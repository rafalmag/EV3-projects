package pl.rafalmag.systemtime;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemTimeManager {

	private static int GET_OFFSET_DELAY_MS = 10000;

	private static Logger log = LoggerFactory
			.getLogger(SystemTimeManager.class);

	private final String ntpServer;

	public SystemTimeManager(String ntpServer) {
		this.ntpServer = ntpServer;
	}

	public long getOffsetMs() throws SystemTimeManagerException {
		NTPUDPClient client = new NTPUDPClient();
		client.setDefaultTimeout(GET_OFFSET_DELAY_MS);
		try {
			client.open();
			try {
				InetAddress hostAddr = InetAddress.getByName(ntpServer);
				TimeInfo info = client.getTime(hostAddr);
				return getOffset(info);
			} catch (IOException e) {
				throw new SystemTimeManagerException("Could not get time from "
						+ ntpServer + ", because of " + e.getMessage(), e);
			} finally {
				client.close();
			}
		} catch (SocketException e) {
			throw new SystemTimeManagerException(
					"Could not open socket, because of " + e.getMessage(), e);
		}
	}

	private long getOffset(TimeInfo info) throws SystemTimeManagerException {
		NtpV3Packet message = info.getMessage();
		logStuff(info, message);
		info.computeDetails(); // compute offset/delay if not already done

		Long offsetValue = info.getOffset();
		Long delayValue = info.getDelay();
		log.info("Roundtrip delay=" + delayValue + "ms, clock offset="
				+ offsetValue + "ms");
		if (offsetValue == null) {
			throw new SystemTimeManagerException(
					"Could not get offset needed to adjust local clock to match remote clock");
		} else {
			return offsetValue;
		}
	}

	private void logStuff(TimeInfo info, NtpV3Packet message) {
		if (!log.isDebugEnabled()) {
			return;
		}
		TimeStamp refNtpTime = message.getReferenceTimeStamp();
		log.debug("Reference Timestamp:\t" + refNtpTime.toDateString());

		// Originate Time is time request sent by client (t1)
		TimeStamp origNtpTime = message.getOriginateTimeStamp();
		log.debug("Originate Timestamp:\t" + origNtpTime.toDateString());

		// Receive Time is time request received by server (t2)
		TimeStamp rcvNtpTime = message.getReceiveTimeStamp();
		log.debug("Receive Timestamp:\t" + rcvNtpTime.toDateString());

		// Transmit time is time reply sent by server (t3)
		TimeStamp xmitNtpTime = message.getTransmitTimeStamp();
		log.debug("Transmit Timestamp:\t" + xmitNtpTime.toDateString());

		// Destination time is time reply received by client (t4)
		TimeStamp destNtpTime = TimeStamp.getNtpTime(info.getReturnTime());
		log.debug("Destination Timestamp:\t" + destNtpTime.toDateString());
	}
}
