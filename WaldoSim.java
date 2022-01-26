import java.lang.Math;


public class Run {

    public static final int MOB_CAP = 60;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static int getNumEndermenInRoll() {

        int mobs = 0;

        int endermen = 0;

        while (mobs <= MOB_CAP) {

            double mob = Math.random();

            int numMobs = 0;

            while (numMobs < 4) {

                if (Math.random() < 0.5) {

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

        int num_enderman_loops = 100_000;

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

        int MAX_TIME = 300;

        for (int i = 0; i < num_tower_loops; i++) {

            double time = 0.0;

            int pearls = 0;

            while (pearls < NUM_PEARLS && time < MAX_TIME) {

                double roll = Math.random();

                int endermen = -1;

                while (roll > 0.0) {

                    roll -= ((double)chances[++endermen])/num_enderman_loops;
                }

                // approximate time to walk off
                time += 0.75;

                // calculated time to fall from y196 to y182
                time += 1.0;

                // calculated ledge time
                time += 3.5;

                

                if (endermen > 0) {

                    time += endermen * 3;

                    time += 7;

                    for (int j = 0; j < endermen; j++) {

                        if (Math.random() < 0.5) {

                            pearls++;
                        }
                    }
                }
            }

            if (pearls >= NUM_PEARLS && time < MAX_TIME) {

                total++;
            }
        }

        System.out.println(((double)total)/num_tower_loops);
    }
}
