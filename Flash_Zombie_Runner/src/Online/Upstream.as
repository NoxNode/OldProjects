package Online {
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import flash.events.NetStatusEvent;
	import flash.geom.Rectangle;
	import flash.media.Camera;
	import flash.media.Microphone;
	import flash.media.Video;
	import flash.net.NetConnection;
	import flash.net.NetStream;
	
	import com.endseven.RTMPStream;
	
	public class Upstream extends RTMPStream {
		
		private var oCamera:Camera;
		private var oMicrophone:Microphone;
		
		/* the constructor. */
		public function Upstream():void {
			
			trace("Upstream object has been created.");
			
			this.oVideo = new Video(640, 480);
			
			this.oConnection = new NetConnection();
			this.oConnection.addEventListener(NetStatusEvent.NET_STATUS, eNetStatus, false, 0, true);
			this.oConnection.connect(this.sMediaServerURL);
			
		}
		
		/* triggered when a net status event is received. */
		private function eNetStatus(oEvent1:NetStatusEvent) {
			
			trace("NetStatusEvent: " + oEvent1.info.code); // debug trace..
			
			switch (oEvent1.info.code) {
			case "NetConnection.Connect.Success":
				
				this.oCamera = Camera.getCamera();
				this.oCamera.setMode(640, 480, 30, true);
				this.oCamera.setQuality(0, 80);
				
				this.oMicrophone = Microphone.getMicrophone();
				
				// attach the camera to the video..
				this.oVideo.attachCamera(this.oCamera);
				this.addChild(oVideo);
				
				// create a stream for the connection..
				this.oNetStream = new NetStream(oConnection);
				
				// attach the camera and microphone to the stream..
				this.oNetStream.attachCamera(this.oCamera);
				this.oNetStream.attachAudio(this.oMicrophone);
				
				// start publishing the stream..
				this.oNetStream.publish(this.sStreamName);
				
				// listen for meta data..
				this.oMetaData.onMetaData = eMetaDataReceived;
				this.oNetStream.client = this.oMetaData;
				
				trace("Connected to the RTMP server."); // debug trace..
				break;
			
			case "NetConnection.Connect.Closed":
			
				trace("Disconnected from the RTMP server."); // debug trace..
				break;
				
			}			
		}		
	}	
}