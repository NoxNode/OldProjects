package Online {
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import flash.display.MovieClip;
	import flash.media.Video;
	import flash.net.NetConnection;
	import flash.net.NetStream;
	
	public class RTMPStream extends MovieClip {
		
		protected var sMediaServerURL:String = "rtmp://[RTMP_STREAM_URL]/[RTMP_APP_NAME]";
		protected var sStreamName:String = "[RTMP_APP_NAME]";
		
		protected var oConnection:NetConnection;
		protected var oMetaData:Object = new Object();
		protected var oNetStream:NetStream;
		protected var oVideo:Video;
		
		/* the constructor */
		public function RTMPStream():void {
			
			NetConnection.prototype.onBWDone = function(oObject1:Object) {
				trace("onBWDone: " + oObject1.toString()); // some media servers are dumb, so we need to catch a strange event..
			}
			
		}
		
		/* triggered when meta data is received. */
		protected function eMetaDataReceived(oObject:Object) {
			trace("MetaData: " + oObject.toString()); // debug trace..
		}
	}
}