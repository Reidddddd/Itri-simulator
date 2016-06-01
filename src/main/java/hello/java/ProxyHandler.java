package hello.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHandler implements InvocationHandler {
  private Object obj;

  public static Object getProxyInstance(Object obj) {
    return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                                  obj.getClass().getInterfaces(), 
                                  new ProxyHandler(obj));
  }

  private ProxyHandler(Object obj) {
    this.obj = obj;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object result;
    System.out.println("before invoke, we can do sth...");
    result = method.invoke(obj, args);
    System.out.println("after invoke, we can do nothing...");
    return result;
  }
}