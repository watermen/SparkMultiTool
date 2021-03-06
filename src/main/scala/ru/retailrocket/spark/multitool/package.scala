package ru.retailrocket.spark

import scala.reflect.ClassTag
import scala.util._

import org.apache.spark.rdd._


package object multitool {
  object Functions {
    def tap[T:ClassTag](f: T => Unit)(o: T) = {f(o); o}
    def applyIf[T:ClassTag](p: Boolean)(f: T => T)(o: T): T = {if(p) f(o) else o}
  }

  object Implicits {
    implicit class MultitoolFunctionsImplicits[T:ClassTag](val self: T) {
      def tap(f: T => Unit) = Functions.tap(f)(self)
      def applyIf(p: Boolean)(f: T => T): T = Functions.applyIf(p)(f)(self)
    }

    implicit class MultitoolRDDFunctionsImplicits[T:ClassTag](val self: RDD[T]) {
      def transform[R:ClassTag](f: T=>Option[R]): RDDFunctions.TransformResult[T,R] = {
        RDDFunctions.transform(f)(self)
      }
      def transform[R:ClassTag](f: T=>R)(implicit d: DummyImplicit): RDDFunctions.TransformResult[T,R] = {
        RDDFunctions.transform(f)(self)
      }
    }
  }
}
