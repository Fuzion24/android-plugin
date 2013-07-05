package android.builder


import sbt._

object AIDL {

  def apply(sourceDirectories:Seq[File],
            idlPath:File,
            platformPath:File,
            managedJavaPath:File,
            javaSource:File):Seq[File] = {

  val aidlPaths = sourceDirectories.map(_ ** "*.aidl").reduceLeft(_ +++ _).get
  if (aidlPaths.isEmpty) {
    //s.log.debug("No AIDL files found, skipping")
    Nil
  } else {
    val processor = aidlPaths.map { ap =>
      idlPath.getAbsolutePath ::
        "-p" + (platformPath / "framework.aidl").absolutePath ::
        "-o" + managedJavaPath.getAbsolutePath ::
        "-I" + javaSource.getAbsolutePath ::
        ap.absolutePath :: Nil
    }.foldLeft(None.asInstanceOf[Option[ProcessBuilder]]) { (f, s) =>
      f match {
        case None => Some(s)
        case Some(first) => Some(first #&& s)
      }
    }.get
    //s.log.debug("generating aidl "+processor)
    processor !

    val rPath = managedJavaPath ** "R.java"
    managedJavaPath ** "*.java" --- (rPath) get
  }
  }
}
