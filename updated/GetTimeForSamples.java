import java.lang.Math;


public class Run {

    public static final int MOB_CAP = 70;

    public static final int UPPER_MOB_CAP = 70;

    public static int total_spider_attempts = 0;

    public static int total_enderman_attempts = 0;

    public static int enderman_attempts_blocked_by_collision = 0;

    public static int spider_attempts_blocked_by_collision = 0;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static final double SPIDER_WEIGHT = 0.24390243902;

    public static final int RADIUS = 42;

    public static final double CHECK_HEIGHT = 79.0;

    public static final double SEE_ENDERMAN_RADIUS = Math.sqrt(Math.pow(87.46,2)-Math.pow(CHECK_HEIGHT,2));

    public static final double SEE_SPIDER_RADIUS = Math.sqrt(Math.max(0.0,Math.pow(78.93,2)-Math.pow(CHECK_HEIGHT,2)));

    public static int[][] mobArray = new int[256][256];

    // public static int endermen = 0;

    public static int getY(int x, int z) {

        return (int) (64 + 2*Math.cos(x / 7.15) + 1*Math.sin(z / 13.45));
        // return (int) (64 + 2*Math.cos(x / 14.3) + 1*Math.sin(z / 26.9));

        // double dist = Math.sqrt(Math.pow(x-128,2)+Math.pow(z-128,2));

        // double dist = Math.abs(x-128) + Math.abs(z-128);

        // if (dist < 20) {

        //     return 61;
        // }

        // if (dist < 29) {

        //     return 62;
        // }
        // if (dist < 38) {

        //     return 63;
        // }

        // if (dist < 47) {

        //     return 64;
        // }

        // if (dist < 56) {

        //     return 65;
        // }
        // if (dist < 65) {

        //     return 66;
        // }
        // return 67;
        // return 64;
    }

    public static void show() {

        for (int x = 100; x < 156; x++) {

            for (int z = 100; z < 156; z++) {

                System.out.print(getY(x, z)-61);
            }
            System.out.println();
        }
    }

    public static int getMob(int x, int z) {

        if (x < 0 || x >= 256) {

            return 0;
        }

        if (z < 0 || z >= 256) {

            return 0;
        }

        return mobArray[x][z];
    }

    public static void setMob(int x, int z, int val) {

        if (x < 0 || x >= 256) {

            return;
        }

        if (z < 0 || z >= 256) {

            return;
        }

        mobArray[x][z] = val;
    }

    public static int[] getNumEndermenInRoll() {

        int mobs = 0;

        int endermen = 0;

        mobArray = new int[256][256];

        int seen_endermen = 0;

        while (mobs <= UPPER_MOB_CAP) {

            double mob = Math.random();

            boolean spider = false;

            if (mob > ENDERMAN_WEIGHT && mob <= (ENDERMAN_WEIGHT+SPIDER_WEIGHT)) {

                spider = true;
            }

            // if (mob <= SPIDER_WEIGHT) {


            // }

            int numMobs = 0;

            int numTries = 0;

            int x = (int)(Math.random()*256);

            int z = (int)(Math.random()*256);

            int y = -1;

            packSpawn:
            while (numTries < 4) {

                numTries++;

                x += ((int)(Math.random()*6)) - ((int)(Math.random()*6));

                z += ((int)(Math.random()*6)) - ((int)(Math.random()*6));

                if (Math.sqrt(Math.pow((x-128),2)+Math.pow(z-128,2)) > RADIUS) {

                    if (mobs < MOB_CAP) {

                        continue;
                    } else if (Math.sqrt(Math.pow((x-128),2)+Math.pow(z-128,2)) > 102.0) {

                        continue;
                    }
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

                if (x < 0 || x >= 256) {

                    continue;
                }

                if (z < 0 || z >= 256) {

                    continue;
                }

                if (y != -1) {

                    if (spider) {

                        total_spider_attempts++;
                        spider_attempts_blocked_by_collision++;

                        if (getY(x, z) != y) {

                            continue;
                        }

                        for (int i = -1; i <= 1; i++) {

                            for (int j = -1; j <= 1; j++) {

                                if (getY(x+i, z+j) > y) {

                                    continue packSpawn;
                                }

                                if (getMob(x+i,z+j) != 0) {

                                    continue packSpawn;
                                }

                                
                            }
                        }

                        spider_attempts_blocked_by_collision--;

                        for (int i = -1; i <= 1; i++) {

                            for (int j = -1; j <= 1; j++) {

                                setMob(x+i, z+j, 1);
                            }
                        }

                        if (Math.sqrt(Math.pow((x-128),2)+Math.pow(z-128,2)) < SEE_SPIDER_RADIUS) {
                            seen_endermen++;
                        }
                    } else {

                        total_enderman_attempts++;
                        enderman_attempts_blocked_by_collision++;

                        if (getMob(x, z) == 1) {
                            continue;
                        }

                        if (getY(x, z) != y) {

                            continue;
                        }

                        enderman_attempts_blocked_by_collision--;

                        if (Math.sqrt(Math.pow((x-128),2)+Math.pow(z-128,2)) < SEE_ENDERMAN_RADIUS) {

                            if (mob <= ENDERMAN_WEIGHT) {
                                seen_endermen++;
                            }
                        }

                        if (Math.sqrt(Math.pow((x-128),2)+Math.pow(z-128,2)) < 60.0) {

                            if (mob <= ENDERMAN_WEIGHT) {
                                endermen++;
                            }
                        }

                        setMob(x, z, 1);
                    }

                    

                    numMobs++;
                }
            }

            // if (mob <= ENDERMAN_WEIGHT) {

            //     endermen += numMobs;
            // }

            mobs += numMobs;
        }

        int[] results = new int[2];

        results[0] = endermen;
        results[1] = endermen;

        return results;
    }

public static void main(String[] args) {

        int num_enderman_loops = 128;

        int num_tower_loops = 50_000;
        // int num_tower_loops = 0;

        // int num_inner = 4;
        // int threshold = 8;

        // double rate = 0.0196;

        // System.out.println((25_000.0*(MOB_CAP/70.0))/(Math.PI*Math.pow(RADIUS,2)));

        int total = 0;

        double total_time = 0.0;


        // int[] chances = new int[MOB_CAP+1];

        int[][] samples = new int[num_enderman_loops][2];

        for(int i = 0; i < num_enderman_loops; i++) {

            samples[i] = getNumEndermenInRoll();
        }

        // samples[0][0] = 2;
        // samples[1][0] = 1;
        // samples[2][0] = 2;
        // samples[3][0] = 0;
        // samples[4][0] = 8;
        // samples[5][0] = 3;
        // samples[6][0] = 4;
        // samples[7][0] = 3;
        // num_enderman_loops = 8;

        // samples[0][0] = 3;
        // samples[1][0] = 5;
        // samples[2][0] = 0;
        // samples[3][0] = 2;
        // samples[4][0] = 0;
        // samples[5][0] = 1;
        // samples[6][0] = 7;
        // samples[7][0] = 1;
        // samples[8][0] = 1;
        // num_enderman_loops = 9;

        // samples[0][0] = 1;
        // samples[1][0] = 1;
        // samples[2][0] = 3;
        // samples[3][0] = 0;
        // samples[4][0] = 2;
        // samples[5][0] = 0;
        // samples[6][0] = 3;
        // samples[7][0] = 5;
        // samples[8][0] = 2;
        // samples[9][0] = 2;
        // samples[10][0] = 0;
        // samples[11][0] = 3;
        // samples[12][0] = 2;
        // samples[13][0] = 2;
        // samples[14][0] = 0;
        // num_enderman_loops = 15;

        // samples[0][0] = 0;
        // samples[1][0] = 0;
        // samples[2][0] = 0;
        // samples[3][0] = 3;
        // samples[4][0] = 4;
        // samples[5][0] = 1;
        // samples[6][0] = 0;
        // samples[7][0] = 0;
        // samples[8][0] = 3;
        // samples[9][0] = 2;
        // samples[10][0] = 0;
        // samples[11][0] = 1;
        // samples[12][0] = 2;
        // samples[13][0] = 2;
        // samples[14][0] = 0;
        // num_enderman_loops = 15;

        samples[0][0] = 0;
        samples[1][0] = 0;
        samples[2][0] = 0;
        samples[3][0] = 3;
        samples[4][0] = 4;
        samples[5][0] = 1;
        samples[6][0] = 0;
        samples[7][0] = 0;
        samples[8][0] = 3;
        samples[9][0] = 2;
        samples[10][0] = 0;
        samples[11][0] = 1;
        samples[12][0] = 2;
        samples[13][0] = 2;
        samples[14][0] = 0;
        num_enderman_loops = 15;

        int NUM_PEARLS = 12;

        int MAX_TIME = 21000;

        for (int i = 0; i < num_tower_loops; i++) {

            double time = 0;

            int pearls = 0;

            int endys = 0;

            int ind = 0;

            while (ind < num_enderman_loops) {

                int[] roll = samples[ind++];

                int seen_endermen = roll[0];

                // approximate time to walk off
                time += 0.25;

                // calculated time to fall from y196 to y182
                time += 1.0;

                // calculated ledge time
                time += (25_000.0*(MOB_CAP/70.0))/(Math.PI*Math.pow(RADIUS,2));

                // time waiting for it to fill up
                time += 3.0;

                // time += 1.0;

                double while_falling_time = (25_000.0*((UPPER_MOB_CAP-MOB_CAP)/70.0))/(Math.PI*Math.pow(Math.sqrt(Math.pow(128,2)-Math.pow(CHECK_HEIGHT+40,2)),2));

                // System.out.println(while_falling_time);

                // calculated time to fall from y182 to y141 (check point)
                time += Math.max(1.8, while_falling_time);

                // approx time spent pausing to check
                // time += 0.5;

                time += 1.4;

                // time spent looking
                // time += 1.0;
                

                // if (seen_endermen > 0 || (seen_endermen > -1 && endys > 0 && 1.75*(time / endys) > (((double)MAX_TIME) / NUM_PEARLS))) {
                // if (seen_endermen > -1) {
                // if (seen_endermen > 0) {
                if (seen_endermen > 0) {

                    int endermen = roll[0];

                    // calculated time to fall from y141 to y72 (platform)
                    

                    // time spent looking for endermen
                    time += 2.0;


                    // time spent baiting/killing endermen
                        // very very rough
                    time += 8.0;
                    time += Math.sqrt(endermen * 32.0);

                    // time spent suiciding after killing endermen
                    time += 2.0;

                    endys += endermen;

                    

                    for (int j = 0; j < endermen; j++) {

                        if (Math.random() < 0.5) {

                            pearls++;
                        }
                    }
                } else {
                    // approx time spent looking for endermen
                    time += 5.0;

                    // approx time moving to suicide
                    time += 0.25;

                    // approx time falling to suicide
                    time += 0.75;
                }

                // approx time for respawn
                time += 1.0;
            }

            // if (pearls >= NUM_PEARLS && time < MAX_TIME) {

            total++;
            total_time += time;
            // }
        }


        System.out.println(((double)total_time)/total);
        System.out.println(((double)total_time)/total/60.0);
        // System.out.println(100.0*((double)spider_attempts_blocked_by_collision)/total_spider_attempts);
        // System.out.println(100.0*((double)enderman_attempts_blocked_by_collision)/total_enderman_attempts);

        // show();
    }
}
