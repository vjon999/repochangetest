package com.ju.it.pratik.img.util;

import java.util.logging.Logger;

public class DCTTransformUtil {

	private static final Logger LOG = Logger.getLogger(DCTTransformUtil.class.getName());
	
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
    
    public double[][] applyDCTImproved(int[][] f, int width, int height) {
    	int[][] temp = new int[N][N];
    	double[][] tempResult = new double[N][N];
    	double[][] result = new double[width][height];
		for(int i=0;i<height;i=i+8) {
			for(int j=0;j<width;j=j+8) {
				//LOG.fine("applyDCTImproved for i="+i+",j="+j);
				for(int k=0;k<N;k++) {
					for(int l=0;l<N;l++) {
						temp[k][l] = f[i+k][j+l];						
					}
				}
				tempResult = applyDCTImproved(temp);
				for(int k=0;k<N;k++) {
					for(int l=0;l<N;l++) {
						result[i+k][j+l] = tempResult[k][l];						
					}
				}
			}
		}
		return result;
    }
    
    public double[][] applyDCTImproved(int[][] f) {
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
    
    public int[][] applyIDCTImproved(double[][] f, int width, int height) {
    	double[][] temp = new double[N][N];
    	int[][] tempResult = new int[N][N];
    	int[][] result = new int[width][height];
		for(int i=0;i<height;i=i+8) {
			for(int j=0;j<width;j=j+8) {
				//LOG.fine("applyIDCTImproved for i="+i+",j="+j);
				for(int k=0;k<N;k++) {
					for(int l=0;l<N;l++) {
						temp[k][l] = f[i+k][j+l];						
					}
				}
				tempResult = applyIDCTImproved(temp);
				for(int k=0;k<N;k++) {
					for(int l=0;l<N;l++) {
						result[i+k][j+l] = tempResult[k][l];						
					}
				}
			}
		}
		return result;
    }
    
    public int[][] applyIDCTImproved(double[][] F) {
        int[][] f = new int[N][N];
        for (int i=0;i<N;i++) {
          for (int j=0;j<N;j++) {
            double sum = 0.0;
            for (int u=0;u<N;u++) {
              for (int v=0;v<N;v++) {
                sum += ((2*c[u]*c[v])/Math.sqrt(N*N)) * DCT_TABLE[i][u] * DCT_TABLE[j][v] * F[u][v];
              }
            }
            f[i][j]=(int)Math.round(sum);
          }
        }
        return f;
    }
    
    public static double computeMiddleBandAvg(double[][] F, int startX, int startY) {
    	double avg = 0;
    	int ctr = 0;
    	for(int i=startY;i<startY+8;i++) {
    		for(int j=startX;j<startX+8-i;j++) {
    			avg += F[i][j];
    			ctr++;
    		}
    	}
    	avg = avg - F[startY + 0][startX + 1] - F[startY + 0][startX + 2] - F[startY + 1][startX + 0] - F[startY + 1][startX + 1] - F[startY + 2][startX + 0];
    	return avg/(ctr - 5);
    }
}
