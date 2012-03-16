package miui.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.MathUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphicsUtils
{
  private static Pattern sAsiaLangPattern;
  private static String[] suffix;

  static
  {
    String[] arrayOfString = new String[40];
    arrayOfString[0] = "老师";
    arrayOfString[1] = "先生";
    arrayOfString[2] = "老板";
    arrayOfString[3] = "仔";
    arrayOfString[4] = "手机";
    arrayOfString[5] = "叔";
    arrayOfString[6] = "阿姨";
    arrayOfString[7] = "宅";
    arrayOfString[8] = "伯";
    arrayOfString[9] = "伯母";
    arrayOfString[10] = "伯父";
    arrayOfString[11] = "哥";
    arrayOfString[12] = "姐";
    arrayOfString[13] = "弟";
    arrayOfString[14] = "妹";
    arrayOfString[15] = "舅";
    arrayOfString[16] = "姑";
    arrayOfString[17] = "父";
    arrayOfString[18] = "主任";
    arrayOfString[19] = "经理";
    arrayOfString[20] = "工作";
    arrayOfString[21] = "同事";
    arrayOfString[22] = "律师";
    arrayOfString[23] = "司机";
    arrayOfString[24] = "师傅";
    arrayOfString[25] = "师父";
    arrayOfString[26] = "爷";
    arrayOfString[27] = "奶";
    arrayOfString[28] = "中介";
    arrayOfString[29] = "董";
    arrayOfString[30] = "总";
    arrayOfString[31] = "太太";
    arrayOfString[32] = "保姆";
    arrayOfString[33] = "某";
    arrayOfString[34] = "秘书";
    arrayOfString[35] = "处长";
    arrayOfString[36] = "局长";
    arrayOfString[37] = "班长";
    arrayOfString[38] = "兄";
    arrayOfString[39] = "助理";
    suffix = arrayOfString;
    sAsiaLangPattern = Pattern.compile("[一-龥]");
  }

  public static Bitmap createNameBitmap(Bitmap paramBitmap, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    Bitmap localBitmap;
    if (paramInt1 <= 0)
      localBitmap = null;
    while (true)
    {
      return localBitmap;
      if (paramInt2 <= 0)
      {
        localBitmap = null;
        continue;
      }
      if (TextUtils.isEmpty(paramString))
      {
        localBitmap = null;
        continue;
      }
      String str1 = getWordFromName(paramString.trim());
      if (str1 == null)
      {
        localBitmap = null;
        continue;
      }
      String str2 = str1.trim();
      if (TextUtils.isEmpty(str2))
      {
        localBitmap = null;
        continue;
      }
      localBitmap = Bitmap.createScaledBitmap(paramBitmap, paramInt1, paramInt2, false);
      Canvas localCanvas = new Canvas(localBitmap);
      paramBitmap.recycle();
      Paint localPaint = new Paint();
      localPaint.setColor(paramInt3);
      if (isChinese(paramString))
      {
        localPaint.setTextSize(0.9F * MathUtils.min(paramInt1, paramInt2));
        localCanvas.drawText(str2, (int)(0.05D * paramInt1), (int)(0.8D * paramInt2), localPaint);
        continue;
      }
      String[] arrayOfString = str2.split(" |\\.|-|,|\\(|\\)|（|）|—");
      if (arrayOfString == null)
      {
        localBitmap.recycle();
        localBitmap = null;
        continue;
      }
      String str3 = null;
      String str4 = null;
      int i = 0;
      if (i < arrayOfString.length)
      {
        if (!TextUtils.isEmpty(arrayOfString[i].trim()))
        {
          if (str3 != null)
            break label239;
          str3 = arrayOfString[i].trim();
        }
        while (true)
        {
          i++;
          break;
          label239: if (str4 != null)
            break label257;
          str4 = arrayOfString[i].trim();
        }
      }
      label257: if (str3 == null)
      {
        localBitmap.recycle();
        localBitmap = null;
        continue;
      }
      if (str4 == null)
      {
        localPaint.setTextSize(0.9F * MathUtils.min(paramInt1, paramInt2));
        localCanvas.drawText(str3, (int)(0.05D * paramInt1), (int)(0.8D * paramInt2), localPaint);
        continue;
      }
      localPaint.setTextSize(0.4F * MathUtils.min(paramInt1, paramInt2));
      localCanvas.drawText(str3, (int)(0.05D * paramInt1), (int)(0.45D * paramInt2), localPaint);
      localCanvas.drawText(str4, (int)(0.3D * paramInt1), (int)(0.9D * paramInt2), localPaint);
    }
  }

  private static String getWordFromName(String paramString)
  {
    String str;
    if (isChinese(paramString))
    {
      str = removeSuffix(paramString);
      if (!TextUtils.isEmpty(str))
        break label23;
    }
    label23: int i;
    for (paramString = null; ; paramString = str.substring(i - 1, i))
    {
      return paramString;
      i = str.length();
    }
  }

  private static boolean isChinese(String paramString)
  {
    return sAsiaLangPattern.matcher(paramString).find();
  }

  public static Bitmap makeRoundImage(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    Bitmap localBitmap;
    if (paramBitmap == null)
      localBitmap = null;
    while (true)
    {
      return localBitmap;
      int i = paramBitmap.getWidth();
      int j = paramBitmap.getHeight();
      localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      Paint localPaint = new Paint(1);
      int k = Math.min(i, j) / 3;
      int m = Math.min(paramInt1, k);
      int n = Math.min(paramInt2, k);
      localCanvas.drawARGB(0, 0, 0, 0);
      localPaint.setColor(-12434878);
      localCanvas.drawRoundRect(new RectF(0.0F, 0.0F, i, j), m, n, localPaint);
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, localPaint);
    }
  }

  private static String removeSuffix(String paramString)
  {
    String str1 = paramString;
    String str2;
    if (TextUtils.isEmpty(str1))
    {
      str2 = null;
      return str2;
    }
    label15: int i = 0;
    label211: for (int j = 0; ; j++)
    {
      if (j < suffix.length)
      {
        if (!str1.endsWith(suffix[j]))
          break label93;
        i = 1;
      }
      for (str1 = str1.substring(0, str1.length() - suffix[j].length()); ; str1 = str1.substring(0, -1 + str1.length()))
      {
        label93: int k;
        do
        {
          if (!TextUtils.isEmpty(str1))
            break label211;
          if ((i != 0) && (!TextUtils.isEmpty(str1)))
            break label15;
          if (str1 != null)
            str1 = str1.trim();
          str2 = str1;
          break;
          k = str1.charAt(-1 + str1.length());
        }
        while (((k < 65) || (k > 90)) && ((k < 97) || (k > 122)) && (k != 44) && (k != 46) && (k != 45) && (k != 40) && (k != 41) && (k != 65288) && (k != 65289) && (k != 8212));
        i = 1;
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.GraphicsUtils
 * JD-Core Version:    0.6.0
 */