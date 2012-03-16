package miui.app.screenelement;

import android.os.FileUtils;
import android.text.TextUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LockscreenConfigFile
{
  private static final String TAG_ROOT = "Config";
  private static final String TAG_TASK = "Intent";
  private static final String TAG_TASKS = "Tasks";
  private static final String TAG_VARIABLE = "Variable";
  private static final String TAG_VARIABLES = "Variables";
  private String mFilePath;
  private HashMap<String, Task> mTasks = new HashMap();
  private HashMap<String, Variable> mVariables = new HashMap();

  private void loadList(Element paramElement, String paramString1, String paramString2, OnLoadElementListener paramOnLoadElementListener)
  {
    Element localElement = Utils.getChild(paramElement, paramString1);
    if (localElement == null);
    while (true)
    {
      return;
      NodeList localNodeList = localElement.getChildNodes();
      for (int i = 0; i < localNodeList.getLength(); i++)
      {
        Node localNode = localNodeList.item(i);
        if ((localNode.getNodeType() != 1) || (!localNode.getNodeName().equals(paramString2)))
          continue;
        paramOnLoadElementListener.OnLoadElement((Element)localNode);
      }
    }
  }

  private void loadTasks(Element paramElement)
  {
    loadList(paramElement, "Tasks", "Intent", new OnLoadElementListener()
    {
      public void OnLoadElement(Element paramElement)
      {
        LockscreenConfigFile.this.putTask(Task.load(paramElement));
      }
    });
  }

  private void loadVariables(Element paramElement)
  {
    loadList(paramElement, "Variables", "Variable", new OnLoadElementListener()
    {
      public void OnLoadElement(Element paramElement)
      {
        LockscreenConfigFile.this.put(paramElement.getAttribute("name"), paramElement.getAttribute("value"), paramElement.getAttribute("type"));
      }
    });
  }

  private void put(String paramString1, String paramString2, String paramString3)
  {
    if ((TextUtils.isEmpty(paramString1)) || (TextUtils.isEmpty(paramString3)));
    while (true)
    {
      return;
      if (("string".equals(paramString3)) || ("number".equals(paramString3)))
      {
        Variable localVariable = (Variable)this.mVariables.get(paramString1);
        if (localVariable == null)
        {
          localVariable = new Variable();
          localVariable.name = paramString1;
          this.mVariables.put(paramString1, localVariable);
        }
        localVariable.type = paramString3;
        localVariable.value = paramString2;
        continue;
      }
    }
  }

  private static void writeTag(FileWriter paramFileWriter, String paramString, boolean paramBoolean)
    throws IOException
  {
    paramFileWriter.write("<");
    if (paramBoolean)
      paramFileWriter.write("/");
    paramFileWriter.write(paramString);
    paramFileWriter.write(">\n");
  }

  private static void writeTag(FileWriter paramFileWriter, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
    throws IOException
  {
    writeTag(paramFileWriter, paramString, paramArrayOfString1, paramArrayOfString2, false);
  }

  private static void writeTag(FileWriter paramFileWriter, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean)
    throws IOException
  {
    paramFileWriter.write("<");
    paramFileWriter.write(paramString);
    int i = 0;
    if (i < paramArrayOfString1.length)
    {
      if ((paramBoolean) && (TextUtils.isEmpty(paramArrayOfString2[i])));
      while (true)
      {
        i++;
        break;
        paramFileWriter.write(" ");
        paramFileWriter.write(paramArrayOfString1[i]);
        paramFileWriter.write("=\"");
        paramFileWriter.write(paramArrayOfString2[i]);
        paramFileWriter.write("\"");
      }
    }
    paramFileWriter.write("/>\n");
  }

  private void writeTasks(FileWriter paramFileWriter)
    throws IOException
  {
    if (this.mTasks.size() == 0);
    while (true)
    {
      return;
      writeTag(paramFileWriter, "Tasks", false);
      String[] arrayOfString1 = new String[7];
      arrayOfString1[0] = Task.TAG_ID;
      arrayOfString1[1] = Task.TAG_ACTION;
      arrayOfString1[2] = Task.TAG_TYPE;
      arrayOfString1[3] = Task.TAG_CATEGORY;
      arrayOfString1[4] = Task.TAG_PACKAGE;
      arrayOfString1[5] = Task.TAG_CLASS;
      arrayOfString1[6] = Task.TAG_NAME;
      Iterator localIterator = this.mTasks.values().iterator();
      while (localIterator.hasNext())
      {
        Task localTask = (Task)localIterator.next();
        String[] arrayOfString2 = new String[7];
        arrayOfString2[0] = localTask.id;
        arrayOfString2[1] = localTask.action;
        arrayOfString2[2] = localTask.type;
        arrayOfString2[3] = localTask.category;
        arrayOfString2[4] = localTask.packageName;
        arrayOfString2[5] = localTask.className;
        arrayOfString2[6] = localTask.name;
        writeTag(paramFileWriter, "Intent", arrayOfString1, arrayOfString2, true);
      }
      writeTag(paramFileWriter, "Tasks", true);
    }
  }

  private void writeVariables(FileWriter paramFileWriter)
    throws IOException
  {
    if (this.mVariables.size() == 0);
    while (true)
    {
      return;
      writeTag(paramFileWriter, "Variables", false);
      String[] arrayOfString1 = new String[3];
      arrayOfString1[0] = "name";
      arrayOfString1[1] = "type";
      arrayOfString1[2] = "value";
      Iterator localIterator = this.mVariables.values().iterator();
      while (localIterator.hasNext())
      {
        Variable localVariable = (Variable)localIterator.next();
        String[] arrayOfString2 = new String[3];
        arrayOfString2[0] = localVariable.name;
        arrayOfString2[1] = localVariable.type;
        arrayOfString2[2] = localVariable.value;
        writeTag(paramFileWriter, "Variable", arrayOfString1, arrayOfString2);
      }
      writeTag(paramFileWriter, "Variables", true);
    }
  }

  public Task getTask(String paramString)
  {
    return (Task)this.mTasks.get(paramString);
  }

  public Collection<Task> getTasks()
  {
    return this.mTasks.values();
  }

  public String getVariable(String paramString)
  {
    Variable localVariable = (Variable)this.mVariables.get(paramString);
    if (localVariable == null);
    for (String str = null; ; str = localVariable.value)
      return str;
  }

  public Collection<Variable> getVariables()
  {
    return this.mVariables.values();
  }

  public boolean load(String paramString)
  {
    int i = 0;
    this.mFilePath = paramString;
    this.mVariables.clear();
    this.mTasks.clear();
    DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    try
    {
      Element localElement = localDocumentBuilderFactory.newDocumentBuilder().parse(new FileInputStream(paramString)).getDocumentElement();
      if ((localElement != null) && (localElement.getNodeName().equals("Config")))
      {
        loadVariables(localElement);
        loadTasks(localElement);
        i = 1;
      }
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      localParserConfigurationException.printStackTrace();
    }
    catch (SAXException localSAXException)
    {
      localSAXException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
    }
    return i;
  }

  public void putNumber(String paramString, double paramDouble)
  {
    putNumber(paramString, Utils.doubleToString(paramDouble));
  }

  public void putNumber(String paramString1, String paramString2)
  {
    put(paramString1, paramString2, "number");
  }

  public void putString(String paramString1, String paramString2)
  {
    put(paramString1, paramString2, "string");
  }

  public void putTask(Task paramTask)
  {
    if ((paramTask == null) || (TextUtils.isEmpty(paramTask.id)));
    while (true)
    {
      return;
      this.mTasks.put(paramTask.id, paramTask);
    }
  }

  public boolean save()
  {
    return save(this.mFilePath);
  }

  public boolean save(String paramString)
  {
    int i = 1;
    try
    {
      FileWriter localFileWriter = new FileWriter(paramString);
      writeTag(localFileWriter, "Config", false);
      writeVariables(localFileWriter);
      writeTasks(localFileWriter);
      writeTag(localFileWriter, "Config", true);
      localFileWriter.flush();
      localFileWriter.close();
      FileUtils.setPermissions(paramString, 511, -1, -1);
      return i;
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        localIOException.printStackTrace();
        i = 0;
      }
    }
  }

  private static abstract interface OnLoadElementListener
  {
    public abstract void OnLoadElement(Element paramElement);
  }

  public class Variable
  {
    public String name;
    public String type;
    public String value;

    public Variable()
    {
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.LockscreenConfigFile
 * JD-Core Version:    0.6.0
 */