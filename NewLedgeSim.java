import java.lang.Math;


public class Run {

    public static final int MOB_CAP = 70;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static final double SPIDER_WEIGHT = 0.24390243902;

    public static int NUM_BLOCKERS = 20;

    public static int[][] mobArray = new int[128][128];

    public static int getY(int x, int z) {

        return (int) (64 + 2*Math.cos(x / 14.7) + 1*Math.sin(z / 26.9));
        // return 64;
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

        int num_enderman_loops = 1;

        int num_tower_loops = 10_000;

        // int num_inner = 4;
        // int threshold = 8;

        // double rate = 0.0196;

        int total = 0;

        int[] chances = new int[MOB_CAP+1];

        for(int i = 0; i < num_enderman_loops; i++) {

            int endermen = getNumEndermenInRoll();

            chances[endermen]++;
        }

        int NUM_PEARLS = 12;

        int MAX_TIME = 210;

        for (int i = 0; i < num_tower_loops; i++) {

            double time = 0;

            int pearls = 0;

            while (pearls < NUM_PEARLS && time < MAX_TIME) {

                int endermen = getNumEndermenInRoll();

                // approximate time to walk off
                time += 0.75;

                // calculated time to fall from y196 to y182
                time += 1.0;

                // calculated ledge time
                time += 1.94;

                // calculated time to fall from y182 to y72 (platform)
                time += 3.2;

                // approx time spent looking for endermen
                time += 1.0;

                if (endermen > 0) {

                    // time spent baiting/killing endermen
                        // very very rough guesses
                    time += 7.5;
                    time += endermen * 2.0;

                    // time spent suiciding after killing endermen
                    time += 1.0;

                    for (int j = 0; j < endermen; j++) {

                        if (Math.random() < 0.5) {

                            pearls++;
                        }
                    }
                } else {
                    // approx time spent looking for endermen
                    time += 1.0;

                    // approx time moving to suicide
                    time += 0.25;

                    // approx time falling to suicide
                    time += 0.75;

                }

                // approx time for respawn
                time += 1.0;
            }

            if (pearls >= NUM_PEARLS && time < MAX_TIME) {

                total++;
            }
        }

        System.out.println(100.0*((double)total)/num_tower_loops);
    }
}
