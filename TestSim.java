import java.lang.Math;


public class Run {

    public static final int MOB_CAP = 70;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static final double SPIDER_WEIGHT = 0.24390243902;

    public static int getY(int x, int z) {

        return (int) (64 + 4*Math.cos(x / 4.7) + 3*Math.sin(z / 6.9));
    }

    public static int getNumEndermenInRoll() {

        int mobs = 0;

        int endermen = 0;

        while (mobs <= MOB_CAP) {

            double mob = Math.random();

            boolean spider = false;

            if (mob > ENDERMAN_WEIGHT && mob <= (ENDERMAN_WEIGHT+SPIDER_WEIGHT)) {

                spider = true;
            }

            // if (mob <= SPIDER_WEIGHT) {


            // }

            int numMobs = 0;

            int x = (int)(Math.random()*128);

            int z = (int)(Math.random()*128);

            int y = -1;

            packSpawn:
            while (numMobs < 4) {

                x += ((int)(Math.random()*6)) - ((int)(Math.random()*6));

                z += ((int)(Math.random()*6)) - ((int)(Math.random()*6));

                // check skylight

                if (15 > ((int)(32*Math.random()))) {

                    break;
                }

                // check block light

                if (4<=((int)(8*Math.random()))) {

                    // good
                } else {

                    break;
                }

                // check if block is same elevation

                if (y == -1) {

                    y = getY(x, z);
                }
                
                if (getY(x, z) == y) {

                    if (spider) {

                        for (int i = -1; i <= 1; i++) {

                            for (int j = -1; j <= 1; j++) {

                                if (getY(x+i, y+j) != y) {

                                    break packSpawn;
                                }
                            }
                        }
                    }

                    numMobs++;
                } else {

                    break;
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

        System.out.println(getY(0, 0));
        System.out.println(getY(0, 1));
        System.out.println(getY(1, 4));

        int num_enderman_loops = 0;

        int num_tower_loops = 0;

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

        int MAX_TIME = 210;

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

                time += 8;

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
