package com.vivekmlresearch.quantumapps.core;

import java.util.*;

public final class Labs {
    private Labs(){}
    public static String grover(){QuantumEngine e=new QuantumEngine(2); e.apply(new QuantumEngine.Gate("H",0,-1));e.apply(new QuantumEngine.Gate("H",1,-1));
        // Oracle for |11>: H on target, controlled X, H on target.
        e.apply(new QuantumEngine.Gate("H",1,-1));e.apply(new QuantumEngine.Gate("CNOT",1,0));e.apply(new QuantumEngine.Gate("H",1,-1));
        for(int q=0;q<2;q++)e.apply(new QuantumEngine.Gate("H",q,-1));
        for(int q=0;q<2;q++)e.apply(new QuantumEngine.Gate("X",q,-1));
        e.apply(new QuantumEngine.Gate("H",1,-1));e.apply(new QuantumEngine.Gate("CNOT",1,0));e.apply(new QuantumEngine.Gate("H",1,-1));
        for(int q=0;q<2;q++)e.apply(new QuantumEngine.Gate("X",q,-1));
        for(int q=0;q<2;q++)e.apply(new QuantumEngine.Gate("H",q,-1));
        return "Grover search • marked |11⟩ • one iteration\n"+e.histogram()+"Ideal statevector demonstration; no hardware speedup claimed.";
    }
    public static String qft(){QuantumEngine e=new QuantumEngine(2);e.apply(new QuantumEngine.Gate("X",0,-1));e.apply(new QuantumEngine.Gate("H",1,-1));
        // Controlled phase pi/2, control q0 and target q1.
        // For input |01>, q0=1 throughout. S(q1) implements the same phase on populated states.
        e.apply(new QuantumEngine.Gate("S",1,-1));e.apply(new QuantumEngine.Gate("H",0,-1));
        e.apply(new QuantumEngine.Gate("SWAP",0,1));
        return "Two-qubit QFT on |01⟩ (little-endian input)\n"+e.state()+"\n"+e.histogram();
    }
    public static String noise(){double p=.12;return String.format(Locale.US,"Bit-flip channel on |0⟩ with p=%.2f:\nP(0)=%.1f%%, P(1)=%.1f%%\n\nPhase-flip on |+⟩ with p=%.2f:\nAfter H measurement: P(0)=%.1f%%, P(1)=%.1f%%\n\nAnalytic single-qubit noise channels; no physical-device calibration.",p,100*(1-p),100*p,p,100*(1-p),100*p);}
    public static String bb84(){Random rng=new Random(1234);int n=1000,matching=0,errors=0;for(int i=0;i<n;i++){int bit=rng.nextInt(2),basis=rng.nextInt(2),bob=rng.nextInt(2);if(basis==bob){matching++;int measured=bit;if(measured!=bit)errors++;}}return "BB84 teaching simulation • seeded 1,000 transmissions\nMatching bases / sifted bits: "+matching+"\nIdeal sifted-key error rate: "+errors+"%\n\nThis is a conceptual simulation, not a secure key exchange or encryption product.";}
    public static String optimization(){StringBuilder s=new StringBuilder("Exact optimization baseline • 4-node traveling salesperson\n");int[][] w={{0,4,7,3},{4,0,2,8},{7,2,0,5},{3,8,5,0}};int best=Integer.MAX_VALUE;String route="";int[] a={1,2,3};do{int cost=w[0][a[0]]+w[a[0]][a[1]]+w[a[1]][a[2]]+w[a[2]][0];if(cost<best){best=cost;route="0 → "+a[0]+" → "+a[1]+" → "+a[2]+" → 0";}}while(next(a));return s.append("Minimum cost: ").append(best).append("\nRoute: ").append(route).append("\nThis exhaustive baseline gives future QAOA experiments an exact reference.").toString();}
    private static boolean next(int[] a){int i=a.length-2;while(i>=0&&a[i]>=a[i+1])i--;if(i<0)return false;int j=a.length-1;while(a[j]<=a[i])j--;int t=a[i];a[i]=a[j];a[j]=t;for(int l=i+1,r=a.length-1;l<r;l++,r--){t=a[l];a[l]=a[r];a[r]=t;}return true;}
    public static String qml(){double[] x={0,.25,.5,.75,1};StringBuilder s=new StringBuilder("One-qubit angle feature map |ψ(x)⟩ = Ry(πx)|0⟩\nPrediction P(1) = sin²(πx/2)\n\n");for(double v:x)s.append(String.format(Locale.US,"x=%.2f  P(1)=%.3f%n",v,Math.pow(Math.sin(Math.PI*v/2),2)));return s.append("\nClassical threshold baseline: x ≥ 0.50. Feature map demo only; no trained quantum neural network or performance advantage claimed.").toString();}
    public static String benchmark(){long t=System.nanoTime();int count=3000;for(int i=0;i<count;i++)QuantumEngine.run(3,QuantumEngine.ghz());long elapsed=System.nanoTime()-t;return String.format(Locale.US,"Local Java statevector • %d runs of three-qubit GHZ\nElapsed: %.2f ms\nThroughput: %.0f circuits/s\n\nDevice-dependent; no comparison with Qiskit, Cirq, PennyLane or CUDA-Q is performed.",count,elapsed/1e6,count*1e9/(double)elapsed);}
}
