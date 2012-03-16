package miui.security;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings.Secure;

public final class ChooseLockSettingsHelper
{
  public static final int DISABLE_ACCESS_CONTROL = 1;
  public static final int DISABLE_AC_FOR_PRIVACY_MODE = 3;
  public static final int DISABLE_PRIVACY_MODE = 4;
  public static final int ENABLE_AC_FOR_PRIVACY_MODE = 2;
  public static final String EXTRA_CONFIRM_PURPOSE = "confirm_purpose";
  public static final String EXTRA_KEY_PASSWORD = "password";
  public static final String FOOTER_TEXT = "com.android.settings.ConfirmLockPattern.footer";
  public static final String FOOTER_WRONG_TEXT = "com.android.settings.ConfirmLockPattern.footer_wrong";
  public static final String HEADER_TEXT = "com.android.settings.ConfirmLockPattern.header";
  public static final String HEADER_WRONG_TEXT = "com.android.settings.ConfirmLockPattern.header_wrong";
  private static final int NO_REQUEST_FOR_ACTIVITY_RESULT = -1024;
  private Activity mActivity;
  private Context mContext;
  private Fragment mFragment;
  private MiuiLockPatternUtils mLockPatternUtils;

  public ChooseLockSettingsHelper(Activity paramActivity)
  {
    this(paramActivity);
    this.mActivity = paramActivity;
  }

  public ChooseLockSettingsHelper(Activity paramActivity, Fragment paramFragment)
  {
    this(paramActivity);
    this.mFragment = paramFragment;
  }

  public ChooseLockSettingsHelper(Context paramContext)
  {
    this.mContext = paramContext;
    this.mLockPatternUtils = new MiuiLockPatternUtils(this.mContext);
  }

  private boolean confirmPassword(int paramInt)
  {
    int i;
    if (!this.mLockPatternUtils.isLockPasswordEnabled())
    {
      i = 0;
      return i;
    }
    Intent localIntent = new Intent();
    localIntent.setClassName("com.android.settings", "com.android.settings.ConfirmLockPassword");
    if (paramInt == -1024)
    {
      localIntent.setFlags(268435456);
      this.mContext.startActivity(localIntent);
    }
    while (true)
    {
      i = 1;
      break;
      if (this.mFragment != null)
      {
        this.mFragment.startActivityForResult(localIntent, paramInt);
        continue;
      }
      this.mActivity.startActivityForResult(localIntent, paramInt);
    }
  }

  private boolean confirmPattern(int paramInt, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    int i;
    if ((!this.mLockPatternUtils.isLockPatternEnabled()) || (!this.mLockPatternUtils.savedPatternExists()))
    {
      i = 0;
      return i;
    }
    Intent localIntent = new Intent();
    localIntent.putExtra("com.android.settings.ConfirmLockPattern.header", paramCharSequence1);
    localIntent.putExtra("com.android.settings.ConfirmLockPattern.footer", paramCharSequence2);
    localIntent.setClassName("com.android.settings", "com.android.settings.ConfirmLockPattern");
    if (paramInt == -1024)
    {
      localIntent.setFlags(268435456);
      this.mContext.startActivity(localIntent);
    }
    while (true)
    {
      i = 1;
      break;
      if (this.mFragment != null)
      {
        this.mFragment.startActivityForResult(localIntent, paramInt);
        continue;
      }
      this.mActivity.startActivityForResult(localIntent, paramInt);
    }
  }

  public boolean isACLockEnabled()
  {
    int i = 1;
    if ((i == Settings.Secure.getInt(this.mContext.getContentResolver(), "access_control_lock_enabled", 0)) && (this.mLockPatternUtils.savedAccessControlExists()));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public boolean isPasswordForPrivacyModeEnabled()
  {
    int i = 1;
    if (i == Settings.Secure.getInt(this.mContext.getContentResolver(), "password_for_privacymode", 0));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public boolean isPrivacyModeEnabled()
  {
    int i = 1;
    if (i == Settings.Secure.getInt(this.mContext.getContentResolver(), "privacy_mode_enabled", 0));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public boolean launchConfirmationActivity(int paramInt, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    boolean bool;
    if (this.mActivity == null)
      bool = false;
    while (true)
    {
      return bool;
      bool = false;
      switch (this.mLockPatternUtils.getKeyguardStoredPasswordQuality())
      {
      default:
        break;
      case 65536:
        bool = confirmPattern(paramInt, paramCharSequence1, paramCharSequence2);
        break;
      case 131072:
      case 262144:
      case 327680:
      case 393216:
        bool = confirmPassword(paramInt);
      }
    }
  }

  public boolean launchConfirmationActivity(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    boolean bool = false;
    switch (this.mLockPatternUtils.getKeyguardStoredPasswordQuality())
    {
    default:
    case 65536:
    case 131072:
    case 262144:
    case 327680:
    case 393216:
    }
    while (true)
    {
      return bool;
      bool = confirmPattern(-1024, paramCharSequence1, paramCharSequence2);
      continue;
      bool = confirmPassword(-1024);
    }
  }

  public void setACLockEnabled(boolean paramBoolean)
  {
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      Settings.Secure.putInt(localContentResolver, "access_control_lock_enabled", i);
      if (!paramBoolean)
        this.mLockPatternUtils.saveACLockPattern(null);
      return;
    }
  }

  public void setPasswordForPrivacyModeEnabled(boolean paramBoolean)
  {
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      Settings.Secure.putInt(localContentResolver, "password_for_privacymode", i);
      return;
    }
  }

  public void setPrivacyModeEnabled(boolean paramBoolean)
  {
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      Settings.Secure.putInt(localContentResolver, "privacy_mode_enabled", i);
      Intent localIntent = new Intent("android.intent.action.PRIVACY_MODE_CHANGED");
      localIntent.putExtra("privacy_mode_enabled", paramBoolean);
      this.mContext.sendBroadcast(localIntent);
      ((ActivityManager)this.mContext.getSystemService("activity")).forceStopPackage("com.android.gallery3d");
      return;
    }
  }

  public MiuiLockPatternUtils utils()
  {
    return this.mLockPatternUtils;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.security.ChooseLockSettingsHelper
 * JD-Core Version:    0.6.0
 */