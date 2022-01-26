import java.lang.Math;


public class Run {

    public static final int MOB_CAP = 60;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static final double SPIDER_WEIGHT = 0.24390243902;

    public static int NUM_BLOCKERS = 20;

    public static int[][] mobArray = new int[128][128];

    public static int getY(int x, int z) {

        // return 64 + (x%7)*3 + (z%9)*4;
        return 64;
    }

    public static int getNumEndermenInRoll() {

        int mobs = 0;

        int endermen = 0;

        mobArray = new int[128][128];

        while (mobs <= MOB_CAP) {

            double mob = Math.random();

            boolean spider = false;

            if (mob > ENDERMAN_WEIGHT && mob <= (ENDERMAN_WEIGHT+SPIDER_WEIGHT)) {

                spider = true;
            }

            // if (mob <= SPIDER_WEIGHT) {


            // }

            int numMobs = 0;

            int numTries = 0;

            int x = (int)(Math.random()*128);

            int z = (int)(Math.random()*128);

            int y = -1;

            packSpawn:
            while (numTries < 4) {

                numTries++;

                x += ((int)(Math.random()*6)) - ((int)(Math.random()*6));

                z += ((int)(Math.random()*6)) - ((int)(Math.random()*6));

                if (Math.sqrt(Math.pow((x-64),2)+Math.pow(z-64,2)) > 64.0) {

                    continue;
                }

                // check skylight

                if (15 > ((int)(32*Math.random()))) {

                    continue;
                }

                // check block light

                if (4<=((int)(8*Math.random()))) {

                    // good
                } else {

                    continue;
                }

                // check if block is same elevation

                if (y == -1) {

                    y = getY(x, z);
                }

                if (x < 0 || x >= 100) {

                    continue;
                }

                if (z < 0 || z >= 100) {

                    continue;
                }
                
                if (y != -1 && getY(x, z) == y) {

                    if (spider) {

                        for (int i = -1; i <= 1; i++) {

                            for (int j = -1; j <= 1; j++) {

                                if (getY(x+i, y+j) > y) {

                                    continue packSpawn;
                                }

                                if (mobArray[x][z] != 0) {

                                    continue packSpawn;
                                }
                            }
                        }

                        for (int i = -1; i <= 1; i++) {

                            for (int j = -1; j <= 1; j++) {

                                mobArray[x][z] = 1;
                            }
                        }
                    } else {

                        if (mobArray[x][z] == 1) {
                            continue packSpawn;
                        }

                        mobArray[x][z] = 1;
                    }

                    

                    numMobs++;
                } else {

                    continue;
                }
            }

            if (mob <= ENDERMAN_WEIGHT) {

                endermen += numMobs;
            }

            mobs += numMobs;
        }

        return endermen;
    }
    public static void main(String[] args) {

        int num_loops = 100_000;

        // int num_inner = 4;
        // int threshold = 8;

        // double rate = 0.0196;

        // int total = 0;

        int[] chances = new int[MOB_CAP+1];

        for(int i = 0; i < num_loops; i++) {

            int endermen = getNumEndermenInRoll();

            chances[endermen]++;
        }

        // System.out.println(total);

        for (int i = 0; i < 20; i++) {

            double res = ((double)chances[i])/num_loops;
            System.out.println(i + ": " + (100.0*res));
        }   
        
    }
}
