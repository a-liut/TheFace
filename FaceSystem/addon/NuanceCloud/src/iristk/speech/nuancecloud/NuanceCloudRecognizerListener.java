/*******************************************************************************
 * Copyright (c) 2014 Gabriel Skantze.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Gabriel Skantze - initial API and implementation
 ******************************************************************************/
package iristk.speech.nuancecloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.sound.sampled.AudioFormat;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import iristk.speech.RecHyp;
import iristk.speech.RecResult;
import iristk.speech.RecognizerListener;
import iristk.speech.ResultPolicy;
import iristk.util.BlockingByteQueue;
import iristk.util.Language;

public class NuanceCloudRecognizerListener implements RecognizerListener {

	public static int REQUEST_TIMEOUT = 5000;
	
	private String DEVICE_ID = "12345";
	private String language = "en_US";
	private String LM = "Dictation";	// or WebSearch
	private String RESULTS_FORMAT = "text/plain"; //"application/xml";

	private static String HOSTNAME = "dictation.nuancemobility.net"; //"dictation.nuancemobility.net"; //"sandbox.nmdp.nuancemobility.net";
	private static String SERVLET = "NMDPAsrCmdServlet/dictation";

	HttpClient httpclient;

	private static String cookie = null;

	BlockingByteQueue speechQueue = new BlockingByteQueue();

	BlockingByteQueue encodedQueue = new BlockingByteQueue();

	private PostThread postThread;
	private boolean newRecognition = false;
	
	private NuanceCloudAudioSource nuanceAudioSource = null;

	private boolean useSpeex = false;

	private AudioFormat audioFormat;

	private int nBestLength = 1;
	
	private ResultPolicy resultPolicy = null;

	private boolean active = true;

	private boolean running;

	private NuanceCloudLicense license;
	
	//private ByteArrayOutputStream out;
	//private AudioFormat audioFormat;

	/*
	 * This function will initialize httpclient, set some basic HTTP parameters (version, UTF),
	 *	and setup SSL settings for communication between the httpclient and our Nuance servers
	 */
	
	public NuanceCloudRecognizerListener(NuanceCloudLicense license) {
		try {
			if (license == null) 
				this.license = NuanceCloudLicense.read();
			else
				this.license = license;
			httpclient = getHttpClient();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public NuanceCloudRecognizerListener() {
		this(null);
	}

	@SuppressWarnings("deprecation")
	private static HttpClient getHttpClient() throws NoSuchAlgorithmException, KeyManagementException
	{
		// Standard HTTP parameters
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUseExpectContinue(params, false);
		// Initialize the HTTP client
		HttpClient httpclient = new DefaultHttpClient(params);
		
		// Initialize/setup SSL
		TrustManager easyTrustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
							throws java.security.cert.CertificateException {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
							throws java.security.cert.CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);
		SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme sch = new Scheme("https", sf, 443);	
		httpclient.getConnectionManager().getSchemeRegistry().register(sch);

		// Return the initialized instance of our httpclient
		return httpclient;
	}

	public void setResultPolicy(ResultPolicy policy) {
		this.resultPolicy = policy;
	}
	
	@Override
	public void recognitionResult(RecResult result) {
		if (!running) return;
		
		if (postThread == null)
			return;
		
		/*
		AudioInputStream ai = new AudioInputStream(new ByteArrayInputStream(out.toByteArray()), audioFormat, out.toByteArray().length);
		try {
			AudioSystem.write(ai, AudioFileFormat.Type.WAVE, new File("c:/" + System.currentTimeMillis() + ".wav"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		
		speechQueue.endWrite();

		//long t = System.currentTimeMillis();

		if (result.isFailed() || (resultPolicy != null && resultPolicy.isReady(result))) {
			postThread.abort();
			postThread = null;
			return;
		}
				
		try {
			postThread.join(REQUEST_TIMEOUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		HttpResponse response = postThread.getResponse();
		//System.out.println(System.currentTimeMillis() - t);

		if (response == null) {
			postThread.abort();
			postThread = null;
			System.out.println("No response from Nuance");
			return;
		}

		postThread = null;
		
		if (result.type == RecResult.MAXSPEECH)
			result.type = RecResult.FINAL;
		
		HttpEntity resEntity = response.getEntity();

		//System.out.println(response.getStatusLine());
		if (resEntity != null) {
			//System.out.println("Response content length: " + resEntity.getContentLength());
			//System.out.println("Chunked?: " + resEntity.isChunked());
			//System.out.println("Nuance Session Id: " + response.getFirstHeader("x-nuance-sessionid").getValue());

			if(cookie == null){
				Header cookieHeader = response.getFirstHeader("Set-Cookie");
				cookie = cookieHeader.getValue();
				StringTokenizer st = new StringTokenizer(cookie,";");
				cookie = st.nextToken().trim();
				//System.out.println("Cookie: " + cookie);
			} 
			if (response.getStatusLine().getStatusCode() == 200) {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(resEntity.getContent(), "UTF-8"));
					String sentence;
					result.nbest = new ArrayList<RecHyp>();
					while ((sentence = reader.readLine()) != null) {
						result.nbest.add(new RecHyp(sentence));
					}
					//System.out.println("Replacing " + result.text + " with " + result.nbest.get(0).text);
					result.text = result.nbest.get(0).text;
					if (nBestLength > 1) {
						while (result.nbest.size() > nBestLength) {
							result.nbest.remove(result.nbest.size()-1);
						}
					} else {
						result.nbest = null;
					}

					result.conf = 1.0f;
					result.sem = null;
					//result.grammar = "NuanceCloud";
					
					EntityUtils.consume(resEntity);
				} catch (Exception ex) {
					ex.printStackTrace();
				} 
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else {
				System.out.println("Nuance returned code " + response.getStatusLine().getStatusCode());
				try {
					EntityUtils.consume(resEntity);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//System.out.println("DONE");
	}

	@Override
	public void speechSamples(byte[] samples, int pos, int len) {
		if (!running) return;
		
		if (newRecognition) {
			newRecognition = false;
			initRequest();
		}
		speechQueue.write(samples, pos, len);
	}
	
	private void initRequest() {
		try {
			speechQueue.reset();

			InputStream inputStream; 
			String codec;
			
			if (!(audioFormat.getSampleRate() == 8000 || audioFormat.getSampleRate() == 16000)) {
				System.err.println("Error: NuanceCloudRecognizer must get 8 or 16 Khz audio, not " + audioFormat.getSampleRate());
			}
			
			if (useSpeex) {
				encodedQueue.reset();
				JSpeexEnc encoder = new JSpeexEnc((int)audioFormat.getSampleRate());
				encoder.startEncoding(speechQueue, encodedQueue);
				inputStream = encodedQueue.getInputStream();
				codec = "audio/x-speex;rate=" + (int)audioFormat.getSampleRate();
			} else {
				inputStream = speechQueue.getInputStream();
				codec = "audio/x-wav;codec=pcm;bit=16;rate=" + (int)audioFormat.getSampleRate();
			}

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("appId", license.getAppId()));
			qparams.add(new BasicNameValuePair("appKey", license.getAppKey()));
			qparams.add(new BasicNameValuePair("id",  DEVICE_ID));
			URI uri = URIUtils.createURI("https", HOSTNAME, 443, SERVLET, URLEncodedUtils.format(qparams, "UTF-8"), null);
			final HttpPost httppost = new HttpPost(uri);
			httppost.addHeader("Content-Type",  codec);
			httppost.addHeader("Content-Language", language);
			httppost.addHeader("Accept-Language", language);
			httppost.addHeader("Accept", RESULTS_FORMAT);
			httppost.addHeader("Accept-Topic", LM);
			if (nuanceAudioSource != null) {
				httppost.addHeader("X-Dictation-AudioSource", nuanceAudioSource.name());
			}
			if (cookie != null)
				httppost.addHeader("Cookie", cookie);
					
			InputStreamEntity reqEntity  = new InputStreamEntity(inputStream, -1);
			reqEntity.setContentType(codec);

			httppost.setEntity(reqEntity);	
			
			postThread = new PostThread(httppost);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private class PostThread extends Thread {

		private HttpResponse response;
		private HttpPost httppost;
		
		public PostThread(HttpPost httppost) {
			this.httppost = httppost;
			start();
		}
		
		@Override
		public void run() {
			try {
				response = httpclient.execute(httppost);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		};
		
		public HttpResponse getResponse() {
			return response;
		}
		
		public void abort() {
			//long t = System.currentTimeMillis();
			httppost.abort();
			try {
				join();
				//System.out.println(System.currentTimeMillis() - t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void initRecognition(AudioFormat audioFormat) {
		
		this.running = false;
		
		if (!active) return;
		
		this.running = true;
		
		this.audioFormat = audioFormat;

		if (postThread != null) {
			System.err.println("ERROR: NuanceCloudRecognizer did not finalize last request");
			recognitionResult(new RecResult(RecResult.FINAL));
		}
		
		postThread = null;
		
		newRecognition  = true;
	}
	
	/*
	public RecResult recognize(AudioSource audioSource) {
		initRecognition(audioSource.getAudioFormat());
		byte[] samples = new byte[3200];
		int len;
		do {
			len = audioSource.read(samples, 0, samples.length);
			if (len > -1)
				speechSamples(samples, 0, len);
		} while (len == 3200);
		RecResult result = new RecResult(RecResult.FINAL);
		recognitionResult(result);
		return result;
	}

	public RecResult recognizeFile(File file) {
		try {
			return recognize(new FileAudioSource(file));
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	*/

	public void setLanguage(Language lang) {
		this.language = lang.getCode().replaceAll("-", "_");
	}

	@Override
	public void startOfSpeech(float timestamp) {
	}

	@Override
	public void endOfSpeech(float timestamp) {
	}

	public void setNBestLength(int length) {
		nBestLength  = length;
	}

	public void setActive(boolean active) {
		this.active = active;
	}



}
