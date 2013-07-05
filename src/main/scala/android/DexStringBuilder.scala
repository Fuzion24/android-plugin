package android.builder

/*
import java.io.{Writer, ByteArrayOutputStream, OutputStream}
import org.jf.dexlib.DexFile
import com.android.application.AndroidApplication
import javax.tools._
import javax.tools.JavaFileObject.Kind
import java.net.URI
import scala.collection._
import scala.collection.JavaConversions._
import scala.Some
import com.android.dx.dex.DexOptions
import com.android.dx.dex.cf.{CfOptions, CfTranslator}
import scala.util.Try
import com.android.application.DalvikExecutable.DexHelper

object DexBuilder extends App {
  type JavaClass = String

  lazy val ANDROID_BUILD_JAR = getClass.getClassLoader.getResource("android_lib/android.jar").getPath

  def apply(classes: List[JavaClass]):Try[DexFile with DexHelper] =
    buildDex(classes.map{ k => (k, getClassName(k))})


  def apply(item: JavaClass): Try[DexFile with DexHelper] =
    apply(List(item))


  private def getClassName(klass: JavaClass):String = {
    val PublicClassPattern = "[\\s]*public[\\s]+class[\\s]([a-zA-Z][a-zA-Z0-9]+).*".r
    val possibleNames = klass.stripMargin.split("\n").flatMap {
      _ match {
        case PublicClassPattern(className) => Some(className)
        case _ => None
      }
    }
    if (possibleNames.length != 1){
      throw new Exception("Unable to parse the public classes found either too many or too little: " + possibleNames)
    }
    possibleNames.head
  }

  private def buildDex(items: List[(String,String)]): Try[DexFile with DexHelper] = {
    val dexFile = dalvikByteCodeCreator(javaCompiler(items))
    AndroidApplication.dexfile(dexFile)
  }

  private def javaCompiler(klasses: List[(String,String)]): Map[String,Array[Byte]] = {
    val compiler = javax.tools.ToolProvider.getSystemJavaCompiler()
    val memFileStore = new MemoryFileManager(compiler)
    val javaSourceFiles =
      for{ (classData,className) <- klasses } yield { new JavaSourceFromString(className,classData) }

    val diagnostics = new DiagnosticCollector[JavaFileObject]()
    val opts = List("-bootclasspath", "./", "-source", "1.5", "-target", "1.5", "-cp", ANDROID_BUILD_JAR)
    val res = compiler.getTask(null, memFileStore, diagnostics,opts,null,javaSourceFiles).call()
    memFileStore.getClassMap
  }

  private def dalvikByteCodeCreator(codeItems: Map[String, Array[Byte]]):Array[Byte] = {
    val cfOpts = new CfOptions
       //Turn off pkg name matching directory structure
       cfOpts.strictNameCheck = false
       cfOpts.optimize = true
    val dOpts  = new DexOptions
    val dFile  = new com.android.dx.dex.file.DexFile(dOpts)

    val clazzes = for {(className, classData) <- codeItems.par} yield
      CfTranslator.translate(className, classData, cfOpts, dOpts)

    clazzes.foreach{dFile.add(_)}

    dFile.toDex(null,false)
  }

  def getDefaultDex: DexFile with DexHelper = {
    val simpleApp = """
      |public class Test {
      |  public static void main(){}
      |}
    """.stripMargin
    apply(simpleApp).get
  }
}

class JavaSourceFromString(name:String, code:String)
  extends SimpleJavaFileObject(URI.create("string:///" + name.replace('.','/') +
    Kind.SOURCE.extension),Kind.SOURCE) {
  protected val bos =
    new ByteArrayOutputStream()

  val w = new java.io.StringWriter()
  def getBytes:Array[Byte] = bos.toByteArray

  override def openOutputStream:OutputStream =  bos
  override def getCharContent(ignoreEncodingErrors: Boolean):CharSequence = code
  override def openWriter:Writer = w
}


class JavaClassObject(name:String, kind:Kind)
  extends SimpleJavaFileObject(URI.create("string:///" + name.replace('.', '/')
    + kind.extension), kind) {
  protected val bos =
    new ByteArrayOutputStream()

  val w = new java.io.StringWriter()
  def getBytes:Array[Byte] = bos.toByteArray

  override def openOutputStream:OutputStream =  bos

  override def openWriter:Writer = w
}

class MemoryFileManager(compiler:JavaCompiler) extends ForwardingJavaFileManager[StandardJavaFileManager](compiler.getStandardFileManager(null, null, null)) {
  private val classMap = scala.collection.mutable.HashMap[String,JavaClassObject]()
  override def getJavaFileForOutput
  (location:javax.tools.JavaFileManager.Location,name:String,kind:Kind, source:FileObject):SimpleJavaFileObject = {
    val jco = new JavaClassObject(name,kind)
    classMap.put(name,jco)
    jco
  }

  override def
   getFileForOutput(location:javax.tools.JavaFileManager.Location,
    packageName:String,
    relativeName:String,
    sibling:FileObject):FileObject =  {
    new JavaClassObject(packageName,Kind.CLASS)
  }

  def getClassMap : scala.collection.Map[String,Array[Byte]] =
    for { (className,javaClassObj) <- classMap } yield { (className, javaClassObj.getBytes) }
}

*/