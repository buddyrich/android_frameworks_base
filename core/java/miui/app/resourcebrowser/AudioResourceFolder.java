package miui.app.resourcebrowser;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.System;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import miui.widget.AsyncAdapter;
import miui.widget.AsyncAdapter.AsyncLoadDataTask;

public class AudioResourceFolder extends ResourceFolder
{
  private static final String[] COMMON_RINGTONE_SUFFIX;
  private Resource mDefaultOption;
  private Uri mDefaultUri;
  private int mDurationLimitation = 2147483647;
  private Resource mMuteOption;
  private int mRingtoneType = this.mMetaData.getInt("android.intent.extra.ringtone.TYPE");

  static
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = ".mp3";
    arrayOfString[1] = ".ogg";
    COMMON_RINGTONE_SUFFIX = arrayOfString;
  }

  public AudioResourceFolder(Context paramContext, Bundle paramBundle, String paramString)
  {
    super(paramContext, paramBundle, paramString);
  }

  private String getTitleForDefaultUri()
  {
    int i = 51118087;
    String str2;
    switch (this.mRingtoneType)
    {
    case 3:
    default:
      String str1 = RingtoneManager.getRingtone(this.mContext, this.mDefaultUri).getTitle(this.mContext);
      Context localContext = this.mContext;
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = str1;
      str2 = removeFileNameSuffix(localContext.getString(51118461, arrayOfObject1));
      if (!TextUtils.isEmpty(str2))
        break;
    case 1:
    case 2:
    case 4:
    }
    Object[] arrayOfObject2;
    for (String str3 = this.mContext.getResources().getString(i); ; str3 = String.format("%s (%s)", arrayOfObject2))
    {
      return str3;
      i = 51118088;
      break;
      i = 51118089;
      break;
      i = 51118090;
      break;
      arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = this.mContext.getResources().getString(i);
      arrayOfObject2[1] = str2;
    }
  }

  private String removeFileNameSuffix(String paramString)
  {
    String str1;
    String[] arrayOfString;
    int i;
    if (!TextUtils.isEmpty(paramString))
    {
      str1 = paramString.toLowerCase();
      arrayOfString = COMMON_RINGTONE_SUFFIX;
      i = arrayOfString.length;
    }
    for (int j = 0; ; j++)
    {
      if (j < i)
      {
        String str2 = arrayOfString[j];
        if (!str1.endsWith(str2))
          continue;
        paramString = paramString.substring(0, str1.lastIndexOf(str2));
      }
      return paramString;
    }
  }

  protected void addItem(String paramString)
  {
    long l = 0L;
    try
    {
      MediaPlayer localMediaPlayer = new MediaPlayer();
      localMediaPlayer.setDataSource(paramString);
      localMediaPlayer.prepare();
      int i = localMediaPlayer.getDuration();
      l = i;
      this.mFileFlags.put(paramString, Long.valueOf(l));
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      while (true)
        localIllegalArgumentException.printStackTrace();
    }
    catch (IllegalStateException localIllegalStateException)
    {
      while (true)
        localIllegalStateException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      while (true)
        l = -1L;
    }
  }

  protected Resource buildResource(String paramString)
  {
    long l = ((Long)this.mFileFlags.get(paramString)).longValue();
    Resource localResource;
    if ((l < 0L) || (l > this.mDurationLimitation))
      localResource = null;
    while (true)
    {
      return localResource;
      localResource = super.buildResource(paramString);
      Bundle localBundle = localResource.getInformation();
      localBundle.putString("m_title", removeFileNameSuffix(new File(paramString).getName()));
      localResource.setInformation(localBundle);
    }
  }

  public void enableDefaultOption(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mDefaultUri = ((Uri)this.mMetaData.getParcelable("android.intent.extra.ringtone.DEFAULT_URI"));
      if (this.mDefaultUri == null);
      switch (this.mRingtoneType)
      {
      case 3:
      default:
        if (this.mDefaultUri != null)
          break;
        this.mDefaultOption = null;
      case 1:
      case 2:
      case 4:
      }
    }
    while (true)
    {
      return;
      this.mDefaultUri = Settings.System.DEFAULT_RINGTONE_URI;
      break;
      this.mDefaultUri = Settings.System.DEFAULT_NOTIFICATION_URI;
      break;
      this.mDefaultUri = Settings.System.DEFAULT_ALARM_ALERT_URI;
      break;
      String str = ResourceHelper.getPathByUri(this.mContext, this.mDefaultUri);
      label150: long l;
      if (TextUtils.isEmpty(str))
      {
        if (new File(this.mDefaultUri.toString()).exists())
          str = this.mDefaultUri.toString();
      }
      else
      {
        File localFile = new File(str);
        if (!localFile.exists())
          break label259;
        l = localFile.length();
      }
      while (true)
      {
        Bundle localBundle = new Bundle();
        localBundle.putString("m_title", getTitleForDefaultUri());
        localBundle.putString("filesize", ResourceHelper.getFormattedSize(this.mContext, l));
        localBundle.putLong("m_lastupdate", 0L);
        localBundle.putString("local_path", this.mDefaultUri.toString());
        this.mDefaultOption = new Resource();
        this.mDefaultOption.setInformation(localBundle);
        break;
        str = "";
        break label150;
        label259: l = 0L;
      }
      this.mDefaultOption = null;
    }
  }

  public void enableMuteOption(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("m_title", this.mContext.getString(51118086));
      localBundle.putString("filesize", ResourceHelper.getFormattedSize(this.mContext, 0L));
      localBundle.putLong("m_lastupdate", 0L);
      localBundle.putString("local_path", "");
      this.mMuteOption = new Resource();
      this.mMuteOption.setInformation(localBundle);
    }
    while (true)
    {
      return;
      this.mMuteOption = null;
    }
  }

  public void loadData(AsyncAdapter<Resource>.AsyncLoadDataTask paramAsyncAdapter)
  {
    if (this.mMuteOption != null)
    {
      Resource[] arrayOfResource2 = new Resource[1];
      arrayOfResource2[0] = this.mMuteOption;
      paramAsyncAdapter.onLoadData(arrayOfResource2);
    }
    if (this.mDefaultOption != null)
    {
      Resource[] arrayOfResource1 = new Resource[1];
      arrayOfResource1[0] = this.mDefaultOption;
      paramAsyncAdapter.onLoadData(arrayOfResource1);
    }
    super.loadData(paramAsyncAdapter);
  }

  public void setDurationLimitation(int paramInt)
  {
    this.mDurationLimitation = paramInt;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.AudioResourceFolder
 * JD-Core Version:    0.6.0
 */