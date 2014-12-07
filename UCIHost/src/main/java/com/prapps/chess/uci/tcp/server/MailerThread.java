package com.prapps.chess.server.uci.tcp;

import java.util.logging.Logger;

import com.prapps.chess.common.engines.UCIUtil;

public class MailerThread implements Runnable {

	private static final Logger LOG = Logger.getLogger(MailerThread.class.getName());

	private ServerConfig serverConfig;

	public MailerThread(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public void run() {
		try {
			String exIp = UCIUtil.getExternalIP();
			UCIUtil.updateExternalIP(exIp, serverConfig.getAdminPort());
			LOG.info(UCIUtil.getExternalIP());
			UCIUtil.mailExternalIP(UCIUtil.getExternalIP() + ":admin_port=" + serverConfig.getAdminPort(), serverConfig.getFromMail(),
					serverConfig.getMailPass(), serverConfig.getToMail());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
