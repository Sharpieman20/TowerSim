import java.lang.Math;


public class Run {

    public static final int MOB_CAP = 50;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static final double SPIDER_WEIGHT = 0.24390243902;

    public static final int NUM_SPAWNING_SPACES = 11357;

    public static int NUM_BLOCKERS = 100;

    public static int getY(int x, int z) {

        return (int) (64 + 2*Math.cos(x / 14.7) + 1*Math.sin(z / 26.9));
    }

    public static int getNumEndermenInRoll() {

        int mobs = 0;

        int endermen = 0;

        NUM_BLOCKERS = 100;

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
                
                if (y != -1 && getY(x, z) == y) {

                    if (spider) {

                        if (((int)(Math.random()*NUM_SPAWNING_SPACES)) < NUM_BLOCKERS*25) {

                            continue packSpawn;
                        }
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

            NUM_BLOCKERS += numMobs;
        }

        return endermen;
    }

    public static void main(String[] args) {

        int num_loops = 1_000_000;

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
