import java.lang.Math;


public class Run {

        public static final int MOB_CAP = 70;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static final double SPIDER_WEIGHT = 0.24390243902;

    public static final int NUM_SPAWNING_SPACES = 6233;

    public static int NUM_CACTI = 20;
    


    public static int getNumEndermenInRoll() {

        int mobs = 0;

        int endermen = 0;

        NUM_CACTI = 20;

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

                        if (((int)(Math.random()*NUM_SPAWNING_SPACES)) < NUM_CACTI*25) {

                            continue packSpawn;
                        }

                        // for (int i = -1; i <= 1; i++) {

                        //     for (int j = -1; j <= 1; j++) {

                        //         if (getY(x+i, y+j) != y) {

                        //             continue packSpawn;
                        //         }
                        //     }
                        // }
                    }

                    numMobs++;
                } else {

                    continue;
                }

                
            }

            if (mob <= ENDERMAN_WEIGHT) {

                endermen += numMobs;
            }

            NUM_CACTI += numMobs;

            mobs += numMobs;
        }

        return endermen;
    }

    public static int getY(int x, int z) {

        // if (x % 16 == 0 && z % 16 == 0) {

        //     // return -1;
        //     return 65;
        // }
        return 64;

        // return (int) (64 + 0*Math.cos(x / 4.7) + 0*Math.sin(z / 6.9));
    }

    public static void main(String[] args) {

        int num_enderman_loops = 1_000_000;

        int num_tower_loops = 10_000;

        // int num_inner = 4;
        // int threshold = 8;

        // double rate = 0.0196;

        int total = 0;

        int numBelowCycles = 0;

        int[] chances = new int[MOB_CAP+1];

        for(int i = 0; i < num_enderman_loops; i++) {

            int endermen = getNumEndermenInRoll();

            chances[endermen]++;
        }

        int NUM_PEARLS = 12;

        int MAX_TIME = 180;

        for (int i = 0; i < num_tower_loops; i++) {

            int time = 0;

            int pearls = 0;

            int cycles = 0;

            while (pearls < NUM_PEARLS && time < MAX_TIME) {

                double roll = Math.random();

                int endermen = -1;

                while (roll > 0.0) {

                    roll -= ((double)chances[++endermen])/num_enderman_loops;
                }

                time += 6;

                time += 4;

                if (endermen > 0) {

                    cycles++;

                    time += endermen * 3;

                    time += 4;

                    for (int j = 0; j < endermen; j++) {

                        if (Math.random() < 0.5) {

                            pearls++;
                        }
                    }
                }
            }

            if (pearls >= NUM_PEARLS && time < MAX_TIME) {

                if (cycles <= 3) {

                    numBelowCycles++;
                }

                total++;
            }
        }

        System.out.println(((double)total)/num_tower_loops);
        System.out.println(((double)numBelowCycles)/num_tower_loops);
    }
}
