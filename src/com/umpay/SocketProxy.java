package com.umpay;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketProxy implements Runnable {
	private Socket req;
	private String targetIP;
	private int targetPort;
	private Socket target;
	private String reqString;
	private String targetString;
	private static int maxProxy = 2147483647;
	private static AtomicInteger activeProxy = new AtomicInteger(0);
	private static ExecutorService threadPool = Executors.newCachedThreadPool();

	private SocketProxy(Socket req, String targetIP, int targetPort) {
		this.req = req;
		this.targetIP = targetIP;
		this.targetPort = targetPort;
	}

	public static void main(String[] args) {
		String targetIP;
		int targetPort;
		ServerSocket ss;
		int port;
		try {
			port = Integer.parseInt(args[0]);
			targetIP = args[1];
			targetPort = Integer.parseInt(args[2]);
			if (args.length > 3)
				maxProxy = Integer.parseInt(args[3]);
		} catch (RuntimeException e) {
			System.err.println("Usage: java com.umpay.util.SocketProxy proxyPort targetIP targetPort [max]");
			return;
		}

		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while (true)
			try {
				Socket sock = ss.accept();
				if (activeProxy.incrementAndGet() > maxProxy) {
					System.out.println("达到最大连接数");
					activeProxy.decrementAndGet();
					try {
						sock.close();
					} catch (IOException localIOException1) {
					}
				} else {
					SocketProxy sp = new SocketProxy(sock, targetIP, targetPort);
					threadPool.execute(sp);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	private String getSockString(Socket sock) {
		String remoteIP = sock.getInetAddress().getHostAddress();
		int remotePort = sock.getPort();
		int localPort = sock.getLocalPort();
		return remoteIP + ":" + remotePort + "/L:" + localPort;
	}

	public void run() {
		try {
			this.target = new Socket(this.targetIP, this.targetPort);
			this.req.setSoTimeout(3600000);

			this.reqString = getSockString(this.req);
			this.targetString = getSockString(this.target);
			System.out.println("new proxy(" + activeProxy.get() + "): "
					+ this.reqString + "<=>" + this.targetString);

			threadPool.execute(new Target2Req());
			InputStream is = this.req.getInputStream();
			OutputStream os = this.target.getOutputStream();
			int bufLen = 10240;
			byte[] buf = new byte[bufLen];
			while (true) {
				int ch = is.read();
				if (ch == -1) {
					throw new EOFException("收到-1");
				}
				buf[0] = ((byte) ch);
				int ava = is.available();
				int r = is.read(buf, 1, ava < bufLen ? ava : bufLen - 1);
				System.out.println(new String(buf,"GBK"));
				os.write(buf, 0, r + 1);
				os.flush();
				System.out.println(this.reqString + " => " + this.targetString
						+ " " + (r + 1) + " bytes.");
			}
		} catch (EOFException localEOFException) {
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("close proxy(" + activeProxy.decrementAndGet()
					+ "): " + this.reqString + "<=>" + this.targetString);
			if(SocketProxy.this.req != null)
				try {
					SocketProxy.this.req.close();
				} catch (IOException localIOException7) {
				}
			if (this.target != null)
				try {
					this.target.close();
				} catch (IOException localIOException8) {
				}
		}
	}

	private class Target2Req implements Runnable {
		private Target2Req() {
		}

		public void run() {
			try {
				InputStream is = SocketProxy.this.target.getInputStream();
				OutputStream os = SocketProxy.this.req.getOutputStream();
				int bufLen = 10240;
				byte[] buf = new byte[bufLen];
				while (true) {
					int ch = is.read();
					if (ch == -1) {
						throw new EOFException("收到-1");
					}
					buf[0] = ((byte) ch);
					int ava = is.available();
					int r = is.read(buf, 1, ava < bufLen ? ava : bufLen - 1);
					System.out.println(new String(buf,"GBK"));
					os.write(buf, 0, r + 1);
					os.flush();
					System.out.println(SocketProxy.this.reqString + " <= "
							+ SocketProxy.this.targetString + " " + (r + 1)
							+ " bytes.");
				}
			} catch (EOFException localEOFException) {
			} catch (SocketException e) {
				System.out.println(e.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(SocketProxy.this.req != null)
					try {
						SocketProxy.this.req.close();
					} catch (IOException localIOException7) {
					}
				if (SocketProxy.this.target != null)
					try {
						SocketProxy.this.target.close();
					} catch (IOException localIOException8) {
					}
			}
		}
	}
}