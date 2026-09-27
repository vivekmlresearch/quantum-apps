package com.vivekmlresearch.quantumapps.core;

import java.util.*;

/** Small, exact statevector simulator. Qubit 0 is the least significant bit. */
public final class QuantumEngine {
    public static final class Gate {
        public final String name; public final int target, control;
        public Gate(String name, int target, int control) { this.name=name.toUpperCase(Locale.ROOT); this.target=target; this.control=control; }
        public String toString() { return name + " q" + target + (control >= 0 ? " ← q" + control : ""); }
    }
    private final int qubits;
    private final double[] re, im;
    public QuantumEngine(int qubits) { if(qubits<1||qubits>8) throw new IllegalArgumentException("1–8 qubits"); this.qubits=qubits; re=new double[1<<qubits]; im=new double[re.length]; re[0]=1; }
    public int qubits(){ return qubits; }
    public void apply(Gate g){
        if(g.target<0||g.target>=qubits) throw new IllegalArgumentException("Target outside circuit");
        int t=1<<g.target;
        switch(g.name){
            case "H": matrix(g.target, Math.sqrt(.5),0,Math.sqrt(.5),0,Math.sqrt(.5),0,-Math.sqrt(.5),0); break;
            case "X": matrix(g.target,0,0,1,0,1,0,0,0); break;
            case "Y": matrix(g.target,0,0,0,-1,0,1,0,0); break;
            case "Z": matrix(g.target,1,0,0,0,0,0,-1,0); break;
            case "S": matrix(g.target,1,0,0,0,0,0,0,1); break;
            case "T": matrix(g.target,1,0,0,0,0,0,Math.sqrt(.5),Math.sqrt(.5)); break;
            case "CNOT": case "CX":
                if(g.control<0||g.control>=qubits||g.control==g.target) throw new IllegalArgumentException("Invalid control");
                for(int i=0;i<re.length;i++) if((i&t)==0&&(i&(1<<g.control))!=0) swap(i,i|t);
                break;
            case "SWAP":
                if(g.control<0||g.control>=qubits||g.control==g.target) throw new IllegalArgumentException("Invalid swap");
                int c=1<<g.control; for(int i=0;i<re.length;i++) if((i&t)==0&&(i&c)!=0) swap(i,(i|t)&~c);
                break;
            default: throw new IllegalArgumentException("Unknown gate: "+g.name);
        }
    }
    private void swap(int a,int b){ double x=re[a];re[a]=re[b];re[b]=x;x=im[a];im[a]=im[b];im[b]=x; }
    private void matrix(int q,double a,double ai,double b,double bi,double c,double ci,double d,double di){
        int bit=1<<q; for(int i=0;i<re.length;i++) if((i&bit)==0){int j=i|bit;double x=re[i],y=im[i],u=re[j],v=im[j];
            re[i]=a*x-ai*y+b*u-bi*v; im[i]=a*y+ai*x+b*v+bi*u;
            re[j]=c*x-ci*y+d*u-di*v; im[j]=c*y+ci*x+d*v+di*u; }
    }
    public double[] probabilities(){double[] p=new double[re.length];for(int i=0;i<p.length;i++)p[i]=re[i]*re[i]+im[i]*im[i];return p;}
    private String bits(int i){String b=Integer.toBinaryString(i);return "0".repeat(qubits-b.length())+b;}
    public String state(){StringBuilder out=new StringBuilder();for(int i=0;i<re.length;i++)if(Math.hypot(re[i],im[i])>1e-8)out.append(String.format(Locale.US,"|%s⟩  %+.4f %+.4fi%n",bits(i),re[i],im[i]));return out.toString();}
    public String histogram(){StringBuilder s=new StringBuilder();double[] p=probabilities();for(int i=0;i<p.length;i++)if(p[i]>1e-8)s.append(String.format(Locale.US,"|%s⟩  %5.1f%%  %s%n",bits(i),100*p[i],"▰".repeat((int)Math.round(p[i]*20))));return s.toString();}
    public static QuantumEngine run(int n,List<Gate> gates){QuantumEngine e=new QuantumEngine(n);for(Gate g:gates)e.apply(g);return e;}
    public static List<Gate> bell(){return Arrays.asList(new Gate("H",0,-1),new Gate("CNOT",1,0));}
    public static List<Gate> ghz(){return Arrays.asList(new Gate("H",0,-1),new Gate("CNOT",1,0),new Gate("CNOT",2,1));}
    /** OpenQASM 2 subset: qreg and gate instructions. Measurement probabilities are shown separately. */
    public static String qasm(int n,List<Gate> gates){StringBuilder s=new StringBuilder("OPENQASM 2.0;\ninclude \"qelib1.inc\";\nqreg q["+n+"];\n");for(Gate g:gates){String v=g.name.equals("CNOT")?"cx":g.name.toLowerCase(Locale.ROOT);s.append(v).append(' ');if(g.control>=0)s.append("q[").append(g.control).append("],");s.append("q[").append(g.target).append("];\n");}return s.toString();}
}
