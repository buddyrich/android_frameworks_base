package miui.util;

public abstract interface PlayerActions
{
  public static final String ACTION_LOCKSCREEN_REQUEST = "lockscreen.action.SONG_METADATA_REQUEST";
  public static final String ACTION_LOCKSRECCN_UPDATE = "lockscreen.action.SONG_METADATA_UPDATED";
  public static final String ACTION_MUSIC_MAIN = "android.intent.action.MUSIC_PLAYER";
  public static final String ACTION_MUSIC_NOWPLAYING_PLAYLIST = "com.miui.player.NOWPLAYING_PLAYLIST";
  public static final String ACTION_MUSIC_PLAYBACK_VIEWER = "com.miui.player.PLAYBACK_VIEWER";
  public static final String BROADCAST_PREVFIX = "com.miui.player";
  public static final String CMDNAME = "command";
  public static final String INIT_GADGET = "com.miui.player.init_gadget";
  public static final String INTENT_KEY_ALBUM = "album";
  public static final String INTENT_KEY_ALBUM_PATH = "album_path";
  public static final String INTENT_KEY_ALBUM_URI = "album_uri";
  public static final String INTENT_KEY_ARTIST = "artist";
  public static final String INTENT_KEY_BLOCK = "block";
  public static final String INTENT_KEY_BUFFER = "buffer";
  public static final String INTENT_KEY_BUFFERED_POS = "buffered_pos";
  public static final String INTENT_KEY_CURRENT_SYSTEM_TIME = "current_system_time";
  public static final String INTENT_KEY_CURRENT_TIME = "current_time";
  public static final String INTENT_KEY_FAVORITE = "favorite";
  public static final String INTENT_KEY_FROM_LAUNCHER = "from_widget";
  public static final String INTENT_KEY_LYRIC = "lyric";
  public static final String INTENT_KEY_LYRIC_STATUS = "lyric_status";
  public static final String INTENT_KEY_LYRIC_TIME = "lyric_time";
  public static final String INTENT_KEY_META_CHANGED_ALBUM = "meta_changed_album";
  public static final String INTENT_KEY_META_CHANGED_BUFFERED_OVER = "meta_changed_buffer";
  public static final String INTENT_KEY_META_CHANGED_LYRIC = "meta_changed_lyric";
  public static final String INTENT_KEY_META_CHANGED_OTHER = "other";
  public static final String INTENT_KEY_META_CHANGED_TRACK = "meta_changed_track";
  public static final String INTENT_KEY_MUSIC_SHOW = "is_showmusic";
  public static final String INTENT_KEY_PLAY = "playing";
  public static final String INTENT_KEY_PROGRESS = "progress";
  public static final String INTENT_KEY_TEMP_ALBUM_BITMAP = "tmp_album_path";
  public static final String INTENT_KEY_TOTAL_TIME = "total_time";
  public static final String INTENT_KEY_TRACK = "track";
  public static final String META_CHANGED = "com.miui.player.metachanged";
  public static final String NEXT_ACTION = "com.miui.player.musicservicecommand.next";
  public static final String PLAYBACK_COMPLETE = "com.miui.player.playbackcomplete";
  public static final String PLAYSTATE_CHANGED = "com.miui.player.playstatechanged";
  public static final String PREVIOUS_ACTION = "com.miui.player.musicservicecommand.previous";
  public static final String QUEUE_CHANGED = "com.miui.player.queuechanged";
  public static final String REFRESH_PROGRESS = "com.miui.player.refreshprogress";
  public static final String REQUEST_PREGRESS = "com.miui.player.requestprogress";
  public static final String SERVICECMD = "com.miui.player.musicservicecommand";
  public static final String TOGGLEPAUSE_ACTION = "com.miui.player.musicservicecommand.togglepause";
  public static final String TOGGLE_FAVORITE = "com.miui.player.togglefavorite";
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.PlayerActions
 * JD-Core Version:    0.6.0
 */