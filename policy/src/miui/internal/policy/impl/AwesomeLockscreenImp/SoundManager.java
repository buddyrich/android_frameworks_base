package com.miui.internal.policy.impl.AwesomeLockScreenImp;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import miui.app.screenelement.ResourceManager;

public class SoundManager
  implements SoundPool.OnLoadCompleteListener
{
  private AudioManager mAudioManager;
  private Context mContext;
  private HashMap<Integer, Boolean> mPendingSoundMap = new HashMap();
  private ArrayList<Integer> mPlayingSoundMap = new ArrayList();
  private ResourceManager mResourceManager;
  private SoundPool mSoundPool;
  private HashMap<String, Integer> mSoundPoolMap = new HashMap();

  public SoundManager(Context paramContext, ResourceManager paramResourceManager)
  {
    this.mResourceManager = paramResourceManager;
    this.mContext = paramContext;
    this.mSoundPool = new SoundPool(4, 3, 100);
    this.mSoundPool.setOnLoadCompleteListener(this);
    this.mAudioManager = ((AudioManager)paramContext.getSystemService("audio"));
  }

  /** @deprecated */
  private void playSoundImp(int paramInt, boolean paramBoolean)
  {
    monitorenter;
    while (true)
    {
      try
      {
        SoundPool localSoundPool = this.mSoundPool;
        if (localSoundPool == null)
          return;
        if ((!paramBoolean) || (this.mPlayingSoundMap.size() == 0))
          break label88;
        Iterator localIterator = this.mPlayingSoundMap.iterator();
        if (localIterator.hasNext())
        {
          Integer localInteger = (Integer)localIterator.next();
          this.mSoundPool.stop(localInteger.intValue());
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      this.mPlayingSoundMap.clear();
      label88: int i = this.mSoundPool.play(paramInt, 1.0F, 1.0F, 1, 0, 1.0F);
      this.mPlayingSoundMap.add(Integer.valueOf(i));
    }
  }

  public void onLoadComplete(SoundPool paramSoundPool, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0)
      playSoundImp(paramInt1, ((Boolean)this.mPendingSoundMap.get(Integer.valueOf(paramInt1))).booleanValue());
    this.mPendingSoundMap.remove(Integer.valueOf(paramInt1));
  }

  /** @deprecated */
  // ERROR //
  public void playSound(String paramString, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 50	com/miui/internal/policy/impl/AwesomeLockScreenImp/SoundManager:mSoundPool	Landroid/media/SoundPool;
    //   6: ifnull +17 -> 23
    //   9: aload_0
    //   10: getfield 43	com/miui/internal/policy/impl/AwesomeLockScreenImp/SoundManager:mContext	Landroid/content/Context;
    //   13: invokestatic 136	miui/util/AudioManagerHelper:isSilentEnabled	(Landroid/content/Context;)Z
    //   16: istore 4
    //   18: iload 4
    //   20: ifeq +6 -> 26
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: aload_0
    //   27: getfield 32	com/miui/internal/policy/impl/AwesomeLockScreenImp/SoundManager:mSoundPoolMap	Ljava/util/HashMap;
    //   30: aload_1
    //   31: invokevirtual 116	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   34: checkcast 88	java/lang/Integer
    //   37: astore 5
    //   39: aload 5
    //   41: ifnonnull +110 -> 151
    //   44: aload_0
    //   45: getfield 41	com/miui/internal/policy/impl/AwesomeLockScreenImp/SoundManager:mResourceManager	Lmiui/app/screenelement/ResourceManager;
    //   48: aload_1
    //   49: invokevirtual 142	miui/app/screenelement/ResourceManager:getFile	(Ljava/lang/String;)Landroid/os/MemoryFile;
    //   52: astore 6
    //   54: aload_0
    //   55: getfield 50	com/miui/internal/policy/impl/AwesomeLockScreenImp/SoundManager:mSoundPool	Landroid/media/SoundPool;
    //   58: aload 6
    //   60: invokevirtual 148	android/os/MemoryFile:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   63: lconst_0
    //   64: aload 6
    //   66: invokevirtual 151	android/os/MemoryFile:length	()I
    //   69: i2l
    //   70: iconst_1
    //   71: invokevirtual 155	android/media/SoundPool:load	(Ljava/io/FileDescriptor;JJI)I
    //   74: invokestatic 106	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   77: astore 5
    //   79: aload_0
    //   80: getfield 32	com/miui/internal/policy/impl/AwesomeLockScreenImp/SoundManager:mSoundPoolMap	Ljava/util/HashMap;
    //   83: aload_1
    //   84: aload 5
    //   86: invokevirtual 159	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   89: pop
    //   90: aload 6
    //   92: invokevirtual 162	android/os/MemoryFile:close	()V
    //   95: aload_0
    //   96: getfield 34	com/miui/internal/policy/impl/AwesomeLockScreenImp/SoundManager:mPendingSoundMap	Ljava/util/HashMap;
    //   99: aload 5
    //   101: iload_2
    //   102: invokestatic 165	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   105: invokevirtual 159	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   108: pop
    //   109: goto -86 -> 23
    //   112: astore_3
    //   113: aload_0
    //   114: monitorexit
    //   115: aload_3
    //   116: athrow
    //   117: astore 7
    //   119: ldc 167
    //   121: new 169	java/lang/StringBuilder
    //   124: dup
    //   125: invokespecial 170	java/lang/StringBuilder:<init>	()V
    //   128: ldc 172
    //   130: invokevirtual 176	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: aload 7
    //   135: invokevirtual 180	java/io/IOException:toString	()Ljava/lang/String;
    //   138: invokevirtual 176	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: invokevirtual 181	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   144: invokestatic 187	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   147: pop
    //   148: goto -53 -> 95
    //   151: aload_0
    //   152: aload 5
    //   154: invokevirtual 91	java/lang/Integer:intValue	()I
    //   157: iload_2
    //   158: invokespecial 123	com/miui/internal/policy/impl/AwesomeLockScreenImp/SoundManager:playSoundImp	(IZ)V
    //   161: goto -138 -> 23
    //
    // Exception table:
    //   from	to	target	type
    //   2	18	112	finally
    //   26	54	112	finally
    //   54	95	112	finally
    //   95	109	112	finally
    //   119	161	112	finally
    //   54	95	117	java/io/IOException
  }

  /** @deprecated */
  public void release()
  {
    monitorenter;
    try
    {
      if (this.mSoundPool != null)
      {
        this.mSoundPoolMap.clear();
        this.mSoundPool.release();
        this.mSoundPool = null;
      }
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.AwesomeLockScreenImp.SoundManager
 * JD-Core Version:    0.6.0
 */