package com.ju.it.pratik.img.util;

public class DCTTransformUtil {

    private double[] c;
    
    private double[][] DCT_TABLE;
    private int N;

    public DCTTransformUtil(int N) {
    	this.N = N;
    	c = new double[N];
    	this.initializeCoefficients(N);
    	
    	DCT_TABLE = new double[N][N];
    	initDCTTable();
    }

    private void initializeCoefficients(int n) {
        for (int i=1;i<n;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
    }
    
    private void initDCTTable() {
    	for (int i=0;i<N;i++) {
            for (int j=0;j<N;j++) {
              DCT_TABLE[i][j] =Math.cos((2*i+1)*j*Math.PI/(2.0*N));
            }
          }
    }
    
    public double[][] applyDCTImproved(double[][] f) {
        double[][] F = new double[N][N];
        for (int u=0;u<N;u++) {
          for (int v=0;v<N;v++) {
            double sum = 0.0;
            for (int i=0;i<N;i++) {
              for (int j=0;j<N;j++) {
                //sum+=Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*f[i][j];
            	  sum += DCT_TABLE[i][u] * DCT_TABLE[j][v] * f[i][j];
              }
            }
            sum *= ((2*c[u]*c[v])/Math.sqrt(N*N));
            F[u][v]=sum;
          }
        }
        return F;
    }

    public double[][] applyDCT(double[][] f) {
        double[][] F = new double[N][N];
        for (int u=0;u<N;u++) {
          for (int v=0;v<N;v++) {
            double sum = 0.0;
            for (int i=0;i<N;i++) {
              for (int j=0;j<N;j++) {
                sum+=Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*f[i][j];
              }
            }
            
            //sum*=((c[u]*c[v])/4.0);
            sum *= (2*c[u]*c[v])/Math.sqrt(N*N);
            //(2*c[u]*c[v])/Math.sqrt(M*N)
            F[u][v]=sum;
          }
        }
        return F;
    }

    public double[][] applyIDCT(double[][] F) {
        double[][] f = new double[N][N];
        for (int i=0;i<N;i++) {
          for (int j=0;j<N;j++) {
            double sum = 0.0;
            for (int u=0;u<N;u++) {
              for (int v=0;v<N;v++) {
                sum += ((2*c[u]*c[v])/Math.sqrt(N*N)) * Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*F[u][v];
              }
            }
            f[i][j]=Math.round(sum);
          }
        }
        return f;
    }
    
    public double[][] applyIDCTImproved(double[][] F) {
        double[][] f = new double[N][N];
        for (int i=0;i<N;i++) {
          for (int j=0;j<N;j++) {
            double sum = 0.0;
            for (int u=0;u<N;u++) {
              for (int v=0;v<N;v++) {
                sum += ((2*c[u]*c[v])/Math.sqrt(N*N)) * DCT_TABLE[i][u] * DCT_TABLE[j][v] * F[u][v];
              }
            }
            f[i][j]=Math.round(sum);
          }
        }
        return f;
    }
}
