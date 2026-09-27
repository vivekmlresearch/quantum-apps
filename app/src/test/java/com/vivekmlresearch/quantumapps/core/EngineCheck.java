package com.vivekmlresearch.quantumapps.core;
import java.util.*;
public class EngineCheck {
    static void close(double actual,double expected){if(Math.abs(actual-expected)>1e-9)throw new AssertionError(actual+" != "+expected);}
    public static void main(String[] args){
        double[] bell=QuantumEngine.run(2,QuantumEngine.bell()).probabilities();close(bell[0],.5);close(bell[1],0);close(bell[2],0);close(bell[3],.5);
        double[] ghz=QuantumEngine.run(3,QuantumEngine.ghz()).probabilities();close(ghz[0],.5);close(ghz[7],.5);
        double[] y=QuantumEngine.run(1,Arrays.asList(new QuantumEngine.Gate("Y",0,-1))).probabilities();close(y[1],1);
        double[] t=QuantumEngine.run(1,Arrays.asList(new QuantumEngine.Gate("H",0,-1),new QuantumEngine.Gate("T",0,-1))).probabilities();close(t[0],.5);close(t[1],.5);
        String g=Labs.grover();if(!g.contains("100.0%"))throw new AssertionError(g);
        String q=QuantumEngine.qasm(2,QuantumEngine.bell());if(!q.contains("cx q[0],q[1];"))throw new AssertionError(q);
        System.out.println("PASS Bell, GHZ, Y, T, Grover, OpenQASM");
    }
}
