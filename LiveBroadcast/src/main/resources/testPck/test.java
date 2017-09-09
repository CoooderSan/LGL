package testPck;

import java.util.Properties;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import util.JedisUtil;
public class test {

	public static void main(String[] args) {
		JedisUtil redis = new JedisUtil();
		redis.getResource();
	}
}
