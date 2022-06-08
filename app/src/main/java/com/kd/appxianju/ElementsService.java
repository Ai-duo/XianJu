package com.kd.appxianju;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.xixun.joey.uart.BytesData;
import com.xixun.joey.uart.IUartListener;
import com.xixun.joey.uart.IUartService;
import java.util.ArrayList;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ElementsService extends Service {

    public IUartService uart;
    StringBuffer builder = new StringBuffer();
    boolean start1 = false,start2 = false,start3 = false,start4 = false,start5 = false;
    boolean dmgd = false, dm1d = false, dmrd = false, dm5d = false, dmsd = false;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            uart = IUartService.Stub.asInterface(iBinder);
            Log.i("TAG_uart", "================ onServiceConnected ====================");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("TAG_uart", "================== onServiceDisconnected ====================");
            uart = null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG_Service", "服务开启");
        //  weaTimer();
       // startVideoDownload();
        bindCardSystemUartAidl();
        startGetUart();
        final String in = "DMGD K8953 2022-06-07 15:06 0000000000000011111111111000000000000000000000101111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 365 368 1501 365 1505 * 31 29 1503 189 166 275 249 240 235 230 *45\n" +
                "*0F\n" +
                "T" +
                "T";
        final String in1 ="DM4D K8953 2022-06-07 15:06 11111000000000000000000000000000000 424 425 1503 423 1502 *34\n" +
                "*FC\n" +
                "T";
        final String in2 ="DMRD K8953 2022-06-07 15:09 111100000000000000000001000000000000000000000011111 276 16 363 1501 1 893 50 996 1502 9 *E7\n" +
                "*C4\n" +
                "T";
        final String in3 ="DM5D K8953 2022-06-07 15:06 111111111111111111111111111111111111111111111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 369 370 1501 369 1505 365 365 1501 364 1504 364 365 1501 364 1504 370 372 1501 370 1504 374 376 1501 374 1506 374 377 1501 373 1505 421 421 1502 420 1501 352 356 1501 352 1506 337 339 1501 337 1505 *52\n" +
                "*1A\n" +
                "T";
        final String in4 ="DMSD K8953 2022-06-07 15:06 111110000 116 93 80 116 138 *B4\n" +
                "*8B\n" +
                "T";
       //getDM1D(in1);
       // getDMGD(in);
       // getDMRD(in2);
       // getDM5D(in3);
       // getDMSD(in4);
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //restartApp();
                // reboot();
                // LiveDataBus.getInstance().getElementsMutableLiveData().postValue(new Elements("金华气象", "2020-11-04 16:35", "12", "23", "23", "56", "56", "54", "124"));
            }
        }).start();*/
    }
    Timer downloadTimer;

    public void startGetUart() {
        startDMGD();
        startDM1D();
        startDMRD();
        startDM5D();
        startDMSD();
    }

    Thread thread,thread1,thread2,thread3,thread4;
    private boolean openPm = false;
    StringBuffer dmgdBuffer = new StringBuffer();
    StringBuffer dm1dBuffer = new StringBuffer();
    StringBuffer dmrdBuffer = new StringBuffer();
    StringBuffer dm5dBuffer = new StringBuffer();
    StringBuffer dmsdBuffer = new StringBuffer();
    String DMGD ="TAG_DMGD";
    String DMRD ="TAG_DMrD";
    String DM1D ="TAG_DM1D";
    String DM5D ="TAG_DM5D";
    private void startDMGD() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(DMGD, "正在获取uart======================");
                } while (null == uart);
                try {
                    //监听/dev/ttyMT2，获取数据/dev/s3c2410_serial3
                    uart.read(port, new IUartListener.Stub() {
                        @Override
                        public void onReceive(BytesData data) throws RemoteException {
                            Log.i(DMGD, "========获取到串口数据===========");
                            for (byte a : data.getData()) {
                                String s1 = "0x" + Integer.toHexString(a & 0xFF) + " ";
                                char ss = (char) a;
                                Log.i(DMGD, "ss:" + ss + ";s1:" + s1);
                                if (start1) {
                                    start1 = true;
                                    dmgdBuffer.append(ss);
                                } else if (ss == 'D') {
                                    dmgd = true;
                                    start1 = true;
                                    dmgdBuffer.append(ss);
                                }
                                Log.i(DMGD, dmgdBuffer.toString());
                                if (dmgdBuffer.length() == 1) {
                                    if (!dmgdBuffer.toString().equals("D")) {
                                        dmgdBuffer.delete(0, dmgdBuffer.length());
                                        dmgd = false;
                                        start1 = false;
                                    }
                                } else if (dmgdBuffer.length() == 2) {
                                    //||!builder.toString().equals("FE")
                                    if (!dmgdBuffer.toString().equals("DM")) {
                                        dmgdBuffer.delete(0, dmgdBuffer.length());
                                        dmgd = false;
                                        start1 = false;
                                    }
                                }else if (dmgdBuffer.length() == 3) {
                                    //||!builder.toString().equals("FE")
                                    if (!dmgdBuffer.toString().equals("DMG")) {
                                        dmgdBuffer.delete(0, dmgdBuffer.length());
                                        dmgd = false;
                                        start1 = false;
                                    }
                                }else if (dmgdBuffer.length() == 4) {
                                    //||!builder.toString().equals("FE")
                                    if (!dmgdBuffer.toString().equals("DMGD")) {
                                        dmgdBuffer.delete(0, dmgdBuffer.length());
                                        dmgd = false;
                                        start1 = false;
                                    }
                                }
                                if (dmgdBuffer.length() > 4 && (dmgd && dmgdBuffer.substring(dmgdBuffer.length() - 1).equals("T"))) {
                                    dmgd = false;
                                    start1 = false;
                                    Log.i(DMGD, dmgdBuffer.toString());
                                    getDMGD(dmgdBuffer.toString());
                                    dmgdBuffer.delete(0, dmgdBuffer.length());
                                }

                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void startDM1D() {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(DM1D, "正在获取uart======================");
                } while (null == uart);
                try {
                    //监听/dev/ttyMT2，获取数据/dev/s3c2410_serial3
                    uart.read(port, new IUartListener.Stub() {
                        @Override
                        public void onReceive(BytesData data) throws RemoteException {
                            Log.i(DM1D, "========获取到串口数据===========");
                            for (byte a : data.getData()) {
                                String s1 = "0x" + Integer.toHexString(a & 0xFF) + " ";
                                char ss = (char) a;
                                Log.i(DM1D, "ss:" + ss + ";s1:" + s1);
                                if (start2) {
                                    start2 = true;
                                    dm1dBuffer.append(ss);
                                } else if (ss == 'D') {
                                    dm1d = true;
                                    start2 = true;
                                    dm1dBuffer.append(ss);
                                }
                                Log.i(DM1D, dm1dBuffer.toString());
                                if (dm1dBuffer.length() == 1) {
                                    if (!dm1dBuffer.toString().equals("D")) {
                                        dm1dBuffer.delete(0, dm1dBuffer.length());
                                        dm1d = false;
                                        start2 = false;
                                    }
                                } else if (dm1dBuffer.length() == 2) {
                                    //||!builder.toString().equals("FE")
                                    if (!dm1dBuffer.toString().equals("DM")) {
                                        dm1dBuffer.delete(0, dm1dBuffer.length());
                                        dm1d = false;
                                        start2 = false;
                                    }
                                }else if (dm1dBuffer.length() == 3) {
                                    if (!dm1dBuffer.toString().equals("DM4")) {
                                        dm1dBuffer.delete(0, dm1dBuffer.length());
                                        dm1d = false;
                                        start2 = false;
                                    }
                                } else if (dm1dBuffer.length() == 4) {
                                    //||!builder.toString().equals("FE")
                                    if (!dm1dBuffer.toString().equals("DM4D")) {
                                        dm1dBuffer.delete(0, dm1dBuffer.length());
                                        dm1d = false;
                                        start2 = false;
                                    }
                                }
                                if (dm1dBuffer.length() > 4 && (dm1d && dm1dBuffer.substring(dm1dBuffer.length() - 1).equals("T"))) {
                                    dm1d = false;
                                    start2 = false;
                                    Log.i(DM1D, dm1dBuffer.toString());
                                    getDM1D(dm1dBuffer.toString());
                                    dm1dBuffer.delete(0, dm1dBuffer.length());
                                }

                            }
                        }

                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
    }

    private void startDMRD() {
        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(DMRD, "正在获取uart======================");
                } while (null == uart);
                try {
                    //监听/dev/ttyMT2，获取数据/dev/s3c2410_serial3
                    uart.read(port, new IUartListener.Stub() {
                        @Override
                        public void onReceive(BytesData data) throws RemoteException {
                            Log.i(DMRD, "========获取到串口数据===========");
                            for (byte a : data.getData()) {
                                String s1 = "0x" + Integer.toHexString(a & 0xFF) + " ";
                                char ss = (char) a;
                                Log.i(DMRD, "ss:" + ss + ";s1:" + s1);
                                if (start3) {
                                    start3 = true;
                                    dmrdBuffer.append(ss);
                                } else if (ss == 'D') {
                                    dmrd = true;
                                    start3 = true;
                                    dmrdBuffer.append(ss);
                                }
                                Log.i(DMRD,  dmrdBuffer.toString());
                                if ( dmrdBuffer.length() == 1) {
                                    if (! dmrdBuffer.toString().equals("D")) {
                                        dmrdBuffer.delete(0,  dmrdBuffer.length());
                                        dmrd = false;
                                        start3 = false;
                                    }
                                } else if ( dmrdBuffer.length() == 2) {
                                    //||!builder.toString().equals("FE")
                                    if (! dmrdBuffer.toString().equals("DM")) {
                                        dmrdBuffer.delete(0,  dmrdBuffer.length());
                                        dmrd = false;
                                        start3 = false;
                                    }
                                }else  if ( dmrdBuffer.length() == 3) {
                                    if (! dmrdBuffer.toString().equals("DMR")) {
                                        dmrdBuffer.delete(0,  dmrdBuffer.length());
                                        dmrd = false;
                                        start3 = false;
                                    }
                                } else if ( dmrdBuffer.length() == 4) {
                                    //||!builder.toString().equals("FE")
                                    if (! dmrdBuffer.toString().equals("DMRD")) {
                                        dmrdBuffer.delete(0,  dmrdBuffer.length());
                                        dmrd = false;
                                        start3 = false;
                                    }
                                }
                                if ( dmrdBuffer.length() > 4 && ( dmrd &&  dmrdBuffer.substring( dmrdBuffer.length() - 1).equals("T"))) {
                                    dmrd = false;
                                    start3 = false;
                                    Log.i(DMRD,  dmrdBuffer.toString());
                                    getDMRD( dmrdBuffer.toString());
                                    dmrdBuffer.delete(0,  dmrdBuffer.length());
                                }

                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
    }

    private void startDM5D() {
        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(DM5D, "正在获取uart======================");
                } while (null == uart);
                try {
                    //监听/dev/ttyMT2，获取数据/dev/s3c2410_serial3
                    uart.read(port, new IUartListener.Stub() {
                        @Override
                        public void onReceive(BytesData data) throws RemoteException {
                            Log.i(DM5D, "========获取到串口数据===========");
                            for (byte a : data.getData()) {
                                String s1 = "0x" + Integer.toHexString(a & 0xFF) + " ";
                                char ss = (char) a;
                                Log.i(DM5D, "ss:" + ss + ";s1:" + s1);
                                if (start4) {
                                    start4 = true;
                                    dm5dBuffer.append(ss);
                                } else if (ss == 'D') {
                                    dm5d = true;
                                    start4 = true;
                                    dm5dBuffer.append(ss);
                                }
                                Log.i(DM5D, dm5dBuffer.toString());
                                if (dm5dBuffer.length() == 1) {
                                    if (!dm5dBuffer.toString().equals("D")) {
                                        dm5dBuffer.delete(0, dm5dBuffer.length());
                                        dm5d = false;
                                        start4 = false;
                                    }
                                } else if (dm5dBuffer.length() == 2) {
                                    //||!builder.toString().equals("FE")
                                    if (!dm5dBuffer.toString().equals("DM")) {
                                        dm5dBuffer.delete(0, dm5dBuffer.length());
                                        dm5d = false;
                                        start4 = false;
                                    }
                                }else if (dm5dBuffer.length() == 3) {
                                    //||!builder.toString().equals("FE")
                                    if (!dm5dBuffer.toString().equals("DM5")) {
                                        dm5dBuffer.delete(0, dm5dBuffer.length());
                                        dm5d = false;
                                        start4 = false;
                                    }
                                }else if (dm5dBuffer.length() == 4) {
                                    //||!builder.toString().equals("FE")
                                    if (!dm5dBuffer.toString().equals("DM5D")) {
                                        dm5dBuffer.delete(0, dm5dBuffer.length());
                                        dm5d = false;
                                        start4 = false;
                                    }
                                }
                                if (dm5dBuffer.length() > 4 && (dm5d && dm5dBuffer.substring(dm5dBuffer.length() - 1).equals("T"))) {
                                    dm5d = false;
                                    start4 = false;
                                    Log.i(DM5D, dm5dBuffer.toString());
                                    getDM5D(dm5dBuffer.toString());
                                    dm5dBuffer.delete(0, dm5dBuffer.length());
                                }

                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        thread3.start();
    }
    private void startDMSD() {
        thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(DM5D, "正在获取uart======================");
                } while (null == uart);
                try {
                    //监听/dev/ttyMT2，获取数据/dev/s3c2410_serial3
                    uart.read(port, new IUartListener.Stub() {
                        @Override
                        public void onReceive(BytesData data) throws RemoteException {
                            Log.i(DM5D, "========获取到串口数据===========");
                            for (byte a : data.getData()) {
                                String s1 = "0x" + Integer.toHexString(a & 0xFF) + " ";
                                char ss = (char) a;
                                Log.i(DM5D, "ss:" + ss + ";s1:" + s1);
                                if (start5) {
                                    start5 = true;
                                    dmsdBuffer.append(ss);
                                } else if (ss == 'D') {
                                    dmsd = true;
                                    start5 = true;
                                    dmsdBuffer.append(ss);
                                }
                                Log.i(DM5D, dmsdBuffer.toString());
                                if (dmsdBuffer.length() == 1) {
                                    if (!dmsdBuffer.toString().equals("D")) {
                                        dmsdBuffer.delete(0, dmsdBuffer.length());
                                        dmsd = false;
                                        start5 = false;
                                    }
                                } else if (dmsdBuffer.length() == 2) {
                                    //||!builder.toString().equals("FE")
                                    if (!dmsdBuffer.toString().equals("DM")) {
                                        dmsdBuffer.delete(0, dmsdBuffer.length());
                                        dmsd = false;
                                        start5 = false;
                                    }
                                }else if (dmsdBuffer.length() == 3) {
                                    //||!builder.toString().equals("FE")
                                    if (!dmsdBuffer.toString().equals("DMS")) {
                                        dmsdBuffer.delete(0, dmsdBuffer.length());
                                        dmsd = false;
                                        start5 = false;
                                    }
                                }else if (dmsdBuffer.length() == 4) {
                                    //||!builder.toString().equals("FE")
                                    if (!dmsdBuffer.toString().equals("DMSD")) {
                                        dmsdBuffer.delete(0, dmsdBuffer.length());
                                        dmsd = false;
                                        start5 = false;
                                    }
                                }
                                if (dmsdBuffer.length() > 4 && (dmsd && dmsdBuffer.substring(dmsdBuffer.length() - 1).equals("T"))) {
                                    dmsd = false;
                                    start5 = false;
                                    Log.i(DM5D, dmsdBuffer.toString());
                                    getDMSD(dmsdBuffer.toString());
                                    dmsdBuffer.delete(0, dmsdBuffer.length());
                                }

                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        thread4.start();
    }
    Timer weaTimer;
/*

    private void weaTimer() {
        weaTimer = new Timer();
        weaTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
                String result = null;
                Request request = new Request.Builder()
                        .url(UrlList.dayurl)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        result = response.body().string();
                        JSONObject json = new JSONObject(result);
                        String Info = json.optString("DATE");
                        String dayinfo = json.optString("wea_txt1");
                        // site_name = json.optString("wea_logo");
                        if (!TextUtils.isEmpty(Info)) {
                            EventBus.getDefault().post(dayinfo);
                            LiveDataBus.getInstance().setWeaInfo(dayinfo);
                            try {
                                Thread.sleep(60 * 60 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Log.i("TAG", "获取天气数据失败");
                    }
                } catch (Exception e) {
                    // Log.i("TAG", "网络异常");
                }

            }
        }, 0, 3000);
    }
*/

    String s2 = "";
    ArrayList<Byte> byteArrayList = new ArrayList<>();
    static int ii = 0;
    boolean PmFlag = false;

    public void bindCardSystemUartAidl() {
        Intent intent = new Intent("xixun.intent.action.UART_SERVICE");
        intent.setPackage("com.xixun.joey.cardsystem");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    ArrayList<Integer> dmgdIndex = new ArrayList<Integer>();
    ArrayList<Integer> dm1dIndex = new ArrayList<Integer>();
    ArrayList<Integer> dm5dIndex = new ArrayList<Integer>();
    ArrayList<Integer> dmrdIndex = new ArrayList<Integer>();
    ArrayList<Integer> dmsdIndex = new ArrayList<Integer>();
 /*  DMGD   气温  湿度
    地温：10cm（47）、20（49）、40（50）、80（51）、160（52）(对应显示10、20、30、40、50)
   */
    public int getDMGDCharCount(String chars) {
        if (TextUtils.isEmpty(chars)) {
            return 0;
        }
        dmgdIndex.clear();
        char[] chars1 = chars.toCharArray();
        int count = 0;
        for (int j = 0; j < chars1.length; j++) {
            if (chars1[j] == '1') {
                count++;
            }
            if (j == 0 || j == 1 || j == 12 || j == 14 || j == 15 || j == 17 || j == 20 || j == 21 || j == 25 || j == 46 || j == 48 || j == 49 || j == 50 || j ==51 || j == 55) {
                dmgdIndex.add(count - 1);
            }
        }
        return count;
    }
    //  DMSD   土壤水分：5（1）、10（2）、20（3）、30（4）、40（5）（对应显示10、20、30、40、50）

    public int getDMSDCharCount(String chars) {
        if (TextUtils.isEmpty(chars)) {
            return 0;
        }
        dmsdIndex.clear();
        char[] chars1 = chars.toCharArray();
        int count = 0;
        for (int j = 0; j < chars1.length; j++) {
            if (chars1[j] == '1') {
                count++;
            }
            if (j ==0 || j == 1 || j == 2 || j == 3 || j == 4) {
                dmsdIndex.add(count - 1);
            }
        }
        return count;
    }
    //水温度52 水盐度58 导电率 60 水溶氧 64 ph 68

    //  DH4D    CO2(1)*/
    public int getDM1DCharCount(String chars) {
        if (TextUtils.isEmpty(chars)) {
            return 0;
        }
        dm1dIndex.clear();
        char[] chars1 = chars.toCharArray();
        int count = 0;
        for (int j = 0; j < chars1.length; j++) {
            if (chars1[j] == '1') {
                count++;
            }
            if (j ==0) {
                dm1dIndex.add(count - 1);
            }
        }
        return count;
    }
    //总辐射 0 紫外线34
   // DMRD   辐射（1）  光合（47）  日照（24）

    public int getDMRDCharCount(String chars) {
        if (TextUtils.isEmpty(chars)) {
            return 0;
        }
        dmrdIndex.clear();
        char[] chars1 = chars.toCharArray();
        int count = 0;
        for (int j = 0; j < chars1.length; j++) {
            if (chars1[j] == '1') {
                count++;
            }
            if (j == 0||j == 50||j == 36||j == 46 ) {
                dmrdIndex.add(count - 1);
            }
        }
        return count;
    }
    //黑球 0，湿球温度 5 人体 10

    //DM5D   9层温度（0.6M温度（1）、1.5M（6）、3M（11）、4.5M（16）、棚外1.5M（21）、棚内左4.5M（26）、中4.5M（31）、右（36）、后（41））

    public int getDM5DCharCount(String chars) {
        if (TextUtils.isEmpty(chars)) {
            return 0;
        }
        dm5dIndex.clear();
        char[] chars1 = chars.toCharArray();
        int count = 0;
        for (int j = 0; j < chars1.length; j++) {
            if (chars1[j] == '1') {
                count++;
            }
            if (j == 0 ||j == 5 || j == 10||j == 15 ||j == 20 || j == 25||j == 30 ||j == 35 || j == 40) {
                dm5dIndex.add(count - 1);
            }
        }
        return count;
    }
    //风向
    String fx = "--";//1
    //风速
    String fs = "--";//2
    //降水
    String js = "--";//13
    //温度
    String wd = "--";
    //日最大温度
    String max_wd = "--";
    //日最小温度
    String min_wd = "--";
    //湿度
    String sd = "--";//21
    //日最小湿度
    String min_sd = "--";
    //气压
    String qy = "--";//26
    //能见度
    String njd = "--";//26
    static String WEA;
    String port = "/dev/ttysWK2";//,/dev/ttysWK2   /dev/ttyMT3
    //判断是否重启,每十分钟判断一次sendCount与currentCount的值，如果两者相等就重起；
    int sendCount = 0, currentCount = -1;
    //j ==52 || j == 58 || j == 60 || j == 64 || j == 68
    //水温度52 水盐度58 导电率 60 水溶氧 64 ph 68
    String swd,syd,ddl,sry,ph,co2;
   public void getDM1D(String info){
       if (TextUtils.isEmpty(info)) {
           return;
       }
       if (info.startsWith("DM4D") && (info.endsWith("F") || info.endsWith("T"))) {

           String[] infoss = info.split(" ");
           for (int i = 0; i < infoss.length; i++) {
               Log.i("TAG_uart", i + ":" + infoss[i]);
           }
           //日期
           String date = infoss[2];
           Log.i("TAG", "日期:" + date);
           //时间
           String time = infoss[3];
           Log.i("TAG", "时间:" + time);
           int count = getDM1DCharCount(infoss[4]);
           int qc = 5;
           if (infoss[5].length() >= 4) {
               qc = 6;
           }
           try {
               if (infoss[4].charAt(0) == '1') {
                   String co21 = infoss[qc + dm1dIndex.get(0)];
                   Log.i("TAG", "co21:" + co21);

                   if (!isNum(co21)) {
                       //fs = "";
                   } else {
                       co2 = Float.parseFloat(co21) + "";
                   }
               }
           } catch (Exception e) {
               Log.i("TAG_", "解析swd时出错");
           }

           LiveDataBus.get().with("dm4d").postValue(co2);
       }

   }
    //总辐射 0 紫外线34
    String zfs,zwx,rzh,gh;
    public void getDMRD(String info){
        if (TextUtils.isEmpty(info)) {
            return;
        }
        if (info.startsWith("DMRD") && (info.endsWith("F") || info.endsWith("T"))) {

            String[] infoss = info.split(" ");
            for (int i = 0; i < infoss.length; i++) {
                Log.i(DMRD, i + ":" + infoss[i]);
            }
            //日期
            String date = infoss[2];
            Log.i(DMRD, "日期:" + date);
            //时间
            String time = infoss[3];
            Log.i(DMRD, "时间:" + time);
            int count = getDMRDCharCount(infoss[4]);
            int qc = 5;
            if (infoss[5].length() >= 4) {
                qc = 6;
            }
            try {
                if (infoss[4].charAt(0) == '1') {
                    String swd1 = infoss[qc + dmrdIndex.get(0)];
                    Log.i(DMRD, "zfs:" + swd1);

                    if (!isNum(swd1)) {
                        //fs = "";
                    } else {
                        zfs = swd1;
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析swd时出错");
            }
            try {
                if (infoss[4].charAt(50) == '1') {
                    String syd1 = infoss[qc + dmrdIndex.get(1)];
                    Log.i(DMRD, "zwx:" + syd1);

                    if (!isNum(syd1)) {
                        //fs = "";
                    } else {
                        rzh = syd1;
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析风速时出错");
            }
            try {
                if (infoss[4].charAt(34) == '1') {
                    String syd1 = infoss[qc + dmrdIndex.get(2)];
                    Log.i(DMRD, "zwx:" + syd1);

                    if (!isNum(syd1)) {
                        //fs = "";
                    } else {
                        zwx = Float.parseFloat(syd1) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析风速时出错");
            }
            try {
                if (infoss[4].charAt(46) == '1') {
                    String syd1 = infoss[qc + dmrdIndex.get(3)];
                    Log.i(DMRD, "zwx:" + syd1);

                    if (!isNum(syd1)) {
                        //fs = "";
                    } else {
                        gh = syd1;
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析风速时出错");
            }

            // LiveDataBus.getInstance().setElements(new Elements("语溪小学气象站", date, time, wd, max_wd, min_wd, sd, min_sd, fx, fs, js, qy, njd));
          //  EventBus.getDefault().post(new Dmrd(zfs,zwx));
            LiveDataBus.get().with("dmrd").postValue(new Dmrd(zfs,zwx,rzh,gh) );
        }

    }
    String sf5,sf10,sf20,sf30,sf40;
    public void getDMSD(String info){
        if (TextUtils.isEmpty(info)) {
            return;
        }
        if (info.startsWith("DMSD") && (info.endsWith("F") || info.endsWith("T"))) {

            String[] infoss = info.split(" ");
            for (int i = 0; i < infoss.length; i++) {
                Log.i(DMRD, i + ":" + infoss[i]);
            }
            //日期
            String date = infoss[2];
            Log.i(DMRD, "日期:" + date);
            //时间
            String time = infoss[3];
            Log.i(DMRD, "时间:" + time);
            int count = getDMSDCharCount(infoss[4]);
            int qc = 5;
            if (infoss[5].length() >= 4) {
                qc = 6;
            }
            try {
                if (infoss[4].charAt(0) == '1') {
                    String sf = infoss[qc + dmsdIndex.get(0)];
                    Log.i(DMRD, "zfs:" + sf);

                    if (!isNum(sf)) {
                        //fs = "";
                    } else {
                        sf5 = Float.parseFloat(sf) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析sf5时出错");
            }
            try {
                if (infoss[4].charAt(1) == '1') {
                    String sf = infoss[qc + dmsdIndex.get(1)];
                    Log.i(DMRD, "zfs:" + sf);

                    if (!isNum(sf)) {
                        //fs = "";
                    } else {
                        sf10 = Float.parseFloat(sf) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析sf5时出错");
            }
            try {
                if (infoss[4].charAt(2) == '1') {
                    String sf = infoss[qc + dmsdIndex.get(2)];
                    Log.i(DMRD, "zfs:" + sf);

                    if (!isNum(sf)) {
                        //fs = "";
                    } else {
                        sf20 = Float.parseFloat(sf) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析sf5时出错");
            }
            try {
                if (infoss[4].charAt(3) == '1') {
                    String sf = infoss[qc + dmsdIndex.get(3)];
                    Log.i(DMRD, "zfs:" + sf);

                    if (!isNum(sf)) {
                        //fs = "";
                    } else {
                        sf30 = Float.parseFloat(sf) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析sf5时出错");
            }
            try {
                if (infoss[4].charAt(4) == '1') {
                    String sf = infoss[qc + dmsdIndex.get(4)];
                    Log.i(DMRD, "zfs:" + sf);

                    if (!isNum(sf)) {
                        //fs = "";
                    } else {
                        sf40 = Float.parseFloat(sf) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMRD, "解析sf5时出错");
            }
            // LiveDataBus.getInstance().setElements(new Elements("语溪小学气象站", date, time, wd, max_wd, min_wd, sd, min_sd, fx, fs, js, qy, njd));
            //  EventBus.getDefault().post(new Dmrd(zfs,zwx));
            LiveDataBus.get().with("dmsd").postValue(new Dmsd( sf5,sf10,sf20,sf30,sf40) );
        }

    }
    //黑球 0，湿球温度 5 人体 10
    String cm06w, cm15w, cm3w, cm45w, cm15pw, lw, mw, rw, bw;
    public void getDM5D(String info){
        if (TextUtils.isEmpty(info)) {
            return;
        }
        if (info.startsWith("DM5D") && (info.endsWith("F") || info.endsWith("T"))) {

            String[] infoss = info.split(" ");
            for (int i = 0; i < infoss.length; i++) {
                Log.i(DM5D, i + ":" + infoss[i]);
            }
            //日期
            String date = infoss[2];
            Log.i(DM5D, "日期:" + date);
            //时间
            String time = infoss[3];
            Log.i(DM5D, "时间:" + time);
            int count = getDM5DCharCount(infoss[4]);
            int qc = 5;
            if (infoss[5].length() >= 4) {
                qc = 6;
            }
            try {
                if (infoss[4].charAt(0) == '1') {
                    String swd1 = infoss[qc + dm5dIndex.get(0)];
                    Log.i(DM5D, "hqwd:" + swd1);
                   /* if (!isNum(swd1)) {
                        //fs = "";
                    } else {
                        hqwd = Float.parseFloat(infoss[qc + dm5dIndex.get(0)]) / 10 + "";
                    }*/
                    if (swd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (swd1.charAt(0) == '-') {
                        if (swd1.length() <= 3) {
                            swd1 = swd1.substring(1, swd1.length());
                            if (isNum(swd1) && Float.parseFloat(swd1) < 580) {
                                cm06w = "-" + Float.parseFloat(swd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(infoss[qc + dm5dIndex.get(0)]) && Float.parseFloat(infoss[qc + dm5dIndex.get(0)]) < 580) {
                            cm06w = Float.parseFloat(infoss[qc + dm5dIndex.get(0)]) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析swd时出错");
            }
            try {
                if (infoss[4].charAt(5) == '1') {
                    String syd1 = infoss[qc + dm5dIndex.get(1)];
                    Log.i(DM5D, "syd:" + syd1);
                    if (syd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (syd1.charAt(0) == '-') {
                        if (syd1.length() <= 3) {
                            syd1 = syd1.substring(1, syd1.length());
                            if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                                cm15w = "-" + Float.parseFloat(syd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(infoss[qc + dm5dIndex.get(1)]) && Float.parseFloat(infoss[qc + dm5dIndex.get(1)]) < 580) {
                            cm15w = Float.parseFloat(infoss[qc + dm5dIndex.get(1)]) / 10 + "";
                        }
                    }
                  /*  if (!isNum(syd1)) {
                        //fs = "";
                    } else {
                        sqwd = Float.parseFloat(infoss[qc + dm5dIndex.get(1)]) / 10 + "";
                    }*/
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析风速时出错");
            }

            try {
                if (infoss[4].charAt(10) == '1') {
                    String syd1 = infoss[qc + dm5dIndex.get(2)];
                    Log.i(DM5D, "rtgz:" + syd1);
                    if (syd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (syd1.charAt(0) == '-') {
                        if (syd1.length() <= 3) {
                            syd1 = syd1.substring(1, syd1.length());
                            if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                                cm3w = "-" + Float.parseFloat(syd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(infoss[qc + dm5dIndex.get(2)]) && Float.parseFloat(infoss[qc + dm5dIndex.get(2)]) < 580) {
                            cm3w = Float.parseFloat(infoss[qc + dm5dIndex.get(2)]) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析雨量时出错");
            }

            try {
                if (infoss[4].charAt(15) == '1') {
                    String syd1 = infoss[qc + dm5dIndex.get(3)];
                    Log.i(DM5D, "4.5:" + syd1);
                    if (syd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (syd1.charAt(0) == '-') {
                        if (syd1.length() <= 3) {
                            syd1 = syd1.substring(1, syd1.length());
                            if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                                cm45w = "-" + Float.parseFloat(syd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                            cm45w = Float.parseFloat(syd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析雨量时出错");
            }
            try {
                if (infoss[4].charAt(20) == '1') {
                    String syd1 = infoss[qc + dm5dIndex.get(4)];
                    Log.i(DM5D, "4.5:" + syd1);
                    if (syd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (syd1.charAt(0) == '-') {
                        if (syd1.length() <= 3) {
                            syd1 = syd1.substring(1, syd1.length());
                            if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                                cm15pw = "-" + Float.parseFloat(syd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                            cm15pw = Float.parseFloat(syd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析雨量时出错");
            }
            try {
                if (infoss[4].charAt(25) == '1') {
                    String syd1 = infoss[qc + dm5dIndex.get(5)];
                    Log.i(DM5D, "4.5:" + syd1);
                    if (syd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (syd1.charAt(0) == '-') {
                        if (syd1.length() <= 3) {
                            syd1 = syd1.substring(1, syd1.length());
                            if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                                lw = "-" + Float.parseFloat(syd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                            lw = Float.parseFloat(syd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析雨量时出错");
            }
            try {
                if (infoss[4].charAt(30) == '1') {
                    String syd1 = infoss[qc + dm5dIndex.get(6)];
                    Log.i(DM5D, "4.5:" + syd1);
                    if (syd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (syd1.charAt(0) == '-') {
                        if (syd1.length() <= 3) {
                            syd1 = syd1.substring(1, syd1.length());
                            if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                                mw = "-" + Float.parseFloat(syd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                            mw = Float.parseFloat(syd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析雨量时出错");
            }
            try {
                if (infoss[4].charAt(35) == '1') {
                    String syd1 = infoss[qc + dm5dIndex.get(7)];
                    Log.i(DM5D, "4.5:" + syd1);
                    if (syd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (syd1.charAt(0) == '-') {
                        if (syd1.length() <= 3) {
                            syd1 = syd1.substring(1, syd1.length());
                            if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                                rw = "-" + Float.parseFloat(syd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                            rw = Float.parseFloat(syd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析雨量时出错");
            }
            try {
                if (infoss[4].charAt(40) == '1') {
                    String syd1 = infoss[qc + dm5dIndex.get(8)];
                    Log.i(DM5D, "4.5:" + syd1);
                    if (syd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (syd1.charAt(0) == '-') {
                        if (syd1.length() <= 3) {
                            syd1 = syd1.substring(1, syd1.length());
                            if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                                bw = "-" + Float.parseFloat(syd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(syd1) && Float.parseFloat(syd1) < 580) {
                            bw = Float.parseFloat(syd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DM5D, "解析雨量时出错");
            }
            // LiveDataBus.getInstance().setElements(new Elements("语溪小学气象站", date, time, wd, max_wd, min_wd, sd, min_sd, fx, fs, js, qy, njd));
          //  EventBus.getDefault().post(new Dm5d(rtgz,sqwd,hqwd));
            LiveDataBus.get().with("dm5d").postValue(new Dm5d( cm06w, cm15w, cm3w, cm45w, cm15pw, lw, mw, rw, bw) );
        }

    }
    String cm10w,cm20w,cm30w,cm40w,cm50w;
    public void getDMGD(String info) {

        Log.i(DMGD, "getElements:");
        if (TextUtils.isEmpty(info)) {
            return;
        }
        if (info.startsWith("DMGD") && (info.endsWith("F") || info.endsWith("T"))) {

            String[] infoss = info.split(" ");
            for (int i = 0; i < infoss.length; i++) {
                Log.i(DMGD, i + ":" + infoss[i]);
            }
            //日期
            String date = infoss[2];
            Log.i(DMGD, "日期:" + date);
            //时间
            String time = infoss[3];
            Log.i(DMGD, "时间:" + time);
            int count = getDMGDCharCount(infoss[4]);
            int qc = 5;
            if (infoss[5].length() >= 4) {
                qc = 6;
            }
            try {
                if (infoss[4].charAt(0) == '1') {
                    String fx1 = infoss[qc + dmgdIndex.get(0)];
                    Log.i(DMGD, "风向:" + fx1);
                    if (isNum(fx1)) {
                        float f = Float.valueOf(fx1);
                        if ((f >= 0 && f < 12.25) || (f > 348.76 && f <= 360)) {
                            fx = "北";
                        } else if (f > 12.26 && f < 33.75) {//22.5
                            fx = "北偏东北";
                        } else if (f > 33.76 && f < 56.25) {
                            fx = "东北";
                        } else if (f > 56.25 && f < 78.75) {
                            fx = "东偏东北";
                        } else if (f > 78.75 && f < 101.25) {
                            fx = "东";
                        } else if (f > 101.25 && f < 123.75) {
                            fx = "东偏东南";
                        } else if (f > 123.76 && f < 146.25) {
                            fx = "东南";
                        } else if (f > 146.26 && f < 168.75) {
                            fx = "南偏东南";
                        } else if (f > 168.75 && f < 191.25) {
                            fx = "南";
                        } else if (f > 191.25 && f < 213.75) {
                            fx = "南偏西南";
                        } else if (f > 213.75 && f < 236.25) {
                            fx = "西南";
                        } else if (f > 236.25 && f < 258.75) {
                            fx = "西偏西南";
                        } else if (f > 258.75 && f < 281.25) {
                            fx = "西";
                        } else if (f > 281.25 && f < 303.75) {
                            fx = "西偏西北";
                        } else if (f > 303.75 && f < 326.25) {
                            fx = "西北";
                        } else if (f > 326.25 && f < 348.75) {
                            fx = "北偏西北";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析风向时出错");
            }
            try {
                if (infoss[4].charAt(1) == '1') {
                    String fs1 = infoss[qc + dmgdIndex.get(1)];
                    Log.i(DMGD, "风速:" + fs1);
                    if (!isNum(fs1)) {
                        //fs = "";
                    } else {
                        fs = Float.parseFloat(infoss[qc + dmgdIndex.get(1)]) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析风速时出错");
            }

            try {
                if (infoss[4].charAt(12) == '1') {
                    String js1 = infoss[qc + dmgdIndex.get(2)];
                    Log.i(DMGD, "降水:" + js1);
                    if (!isNum(js1)) {
                        // js = "";
                    } else {
                        js = Float.parseFloat(infoss[qc + dmgdIndex.get(2)]) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析雨量时出错");
            }

            try {
                if (infoss[4].charAt(14) == '1') {
                    String wd1 = infoss[qc + dmgdIndex.get(3)];
                    if (wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (wd1.charAt(0) == '-') {
                        if (wd1.length() <= 3) {
                            wd1 = wd1.substring(1, wd1.length());
                            if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                                wd = "-" + Float.parseFloat(wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                            wd = Float.parseFloat(wd1) / 10 + "";
                        }
                    }
                    Log.i(DMGD, "温度:" + wd);
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析温度时出错");
            }
            try {
                if (infoss[4].charAt(15) == '1') {
                    String max_wd1 = infoss[qc + dmgdIndex.get(4)];
                    if (max_wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (max_wd1.charAt(0) == '-') {
                        if (max_wd1.length() <= 3) {
                            max_wd1 = max_wd1.substring(1, max_wd1.length());
                            if (isNum(max_wd1) && Float.parseFloat(max_wd1) < 580) {
                                max_wd = "-" + Float.parseFloat(max_wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(infoss[qc + dmgdIndex.get(4)]) && Float.parseFloat(infoss[qc + dmgdIndex.get(4)]) < 580) {
                            max_wd = Float.parseFloat(infoss[qc + dmgdIndex.get(4)]) / 10 + "";
                        }
                    }
                    Log.i(DMGD, "日最高温度:" + max_wd);
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析温度时出错");
            }
            try {
                if (infoss[4].charAt(17) == '1') {
                    String min_wd1 = infoss[qc + dmgdIndex.get(5)];
                    if (min_wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (min_wd1.charAt(0) == '-') {
                        if (min_wd1.length() <= 3) {
                            min_wd1 = min_wd1.substring(1, min_wd1.length());
                            if (isNum(min_wd1) && Float.parseFloat(min_wd1) < 580) {
                                min_wd = "-" + Float.parseFloat(min_wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(infoss[qc + dmgdIndex.get(5)]) && Float.parseFloat(infoss[qc + dmgdIndex.get(5)]) < 580) {
                            min_wd = Float.parseFloat(infoss[qc + dmgdIndex.get(5)]) / 10 + "";
                        }
                    }
                    Log.i(DMGD, "日最低温度:" + min_wd);
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析温度时出错");
            }
            try {
                if (infoss[4].charAt(20) == '1') {
                    String sd1 = infoss[qc + dmgdIndex.get(6)];
                    Log.i(DMGD, "湿度:" + sd1);
                    if (!isNum(sd1)) {
                        //sd = "";
                    } else {
                        sd = sd1;
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析湿度时出错");
            }
            try {
                if (infoss[4].charAt(21) == '1') {
                    String min_sd1 = infoss[qc + dmgdIndex.get(7)];
                    Log.i("TAG", "湿度:" + min_sd1);
                    if (!isNum(min_sd1)) {
                        //sd = "";
                    } else {
                        min_sd = min_sd1;
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析湿度时出错");
            }
            try {
                if (infoss[4].charAt(25) == '1') {
                    String qy1 = infoss[qc + dmgdIndex.get(8)];
                    Log.i(DMGD, "气压:" + qy1);
                    if (!isNum(qy1)) {
                        //qy = "";
                    } else {
                        qy = Float.parseFloat(infoss[qc + dmgdIndex.get(8)]) / 10 + "";
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析气压时出错");
            }
            try {
                if (infoss[4].charAt(46) == '1') {
                    String wd1 = infoss[qc + dmgdIndex.get(9)];
                    if (wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (wd1.charAt(0) == '-') {
                        if (wd1.length() <= 3) {
                            wd1 = wd1.substring(1, wd1.length());
                            if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                                cm10w = "-" + Float.parseFloat(wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                            cm10w = Float.parseFloat(wd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析10cm温度时出错");
            }
            try {
                if (infoss[4].charAt(48) == '1') {
                    String wd1 = infoss[qc + dmgdIndex.get(10)];
                    if (wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (wd1.charAt(0) == '-') {
                        if (wd1.length() <= 3) {
                            wd1 = wd1.substring(1, wd1.length());
                            if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                                cm20w = "-" + Float.parseFloat(wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                            cm20w = Float.parseFloat(wd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析20cm温度时出错");
            }
            try {
                if (infoss[4].charAt(49) == '1') {
                    String wd1 = infoss[qc + dmgdIndex.get(11)];
                    if (wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (wd1.charAt(0) == '-') {
                        if (wd1.length() <= 3) {
                            wd1 = wd1.substring(1, wd1.length());
                            if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                                cm30w = "-" + Float.parseFloat(wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                            cm30w = Float.parseFloat(wd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析30cm温度时出错");
            }
            try {
                if (infoss[4].charAt(50) == '1') {
                    String wd1 = infoss[qc + dmgdIndex.get(12)];
                    if (wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (wd1.charAt(0) == '-') {
                        if (wd1.length() <= 3) {
                            wd1 = wd1.substring(1, wd1.length());
                            if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                                cm40w = "-" + Float.parseFloat(wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                            cm40w = Float.parseFloat(wd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析40cm温度时出错");
            }
            try {
                if (infoss[4].charAt(51) == '1') {
                    String wd1 = infoss[qc + dmgdIndex.get(13)];
                    if (wd1.charAt(0) == '/') {
                        //wd = " ";
                    } else if (wd1.charAt(0) == '-') {
                        if (wd1.length() <= 3) {
                            wd1 = wd1.substring(1, wd1.length());
                            if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                                cm50w = "-" + Float.parseFloat(wd1) / 10;
                            }
                        }
                    } else {
                        if (isNum(wd1) && Float.parseFloat(wd1) < 580) {
                            cm50w = Float.parseFloat(wd1) / 10 + "";
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(DMGD, "解析50cm温度时出错");
            }
            try {
                if (infoss[4].charAt(55) == '1') {
                    //能见度
                    String njd1 = "";
                    njd1 = infoss[qc + dmgdIndex.get(9)];
                    Log.i(DMGD, "能见度:" + njd1);
                    if (!isNum(njd1)) {
                        //njd = "12345";
                    } else {
                        njd = njd1;
                    }

                }
            } catch (Exception e) {
                Log.i(DMGD, "解析能见度时出错");
            }
            // LiveDataBus.getInstance().setElements(new Elements("语溪小学气象站", date, time, wd, max_wd, min_wd, sd, min_sd, fx, fs, js, qy, njd));
            LiveDataBus.get().with("dmgd").postValue(new Dmgd( wd,sd,cm10w,cm20w,cm30w,cm40w,cm50w) );

        }

    }

    private Timer timerListener;


    public boolean isNum(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) str);
        boolean result = matcher.matches();
        return result;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unbindService(conn);
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        super.onDestroy();
    }

    public String stringToGbk(String string) throws Exception {
        byte[] bytes = new byte[string.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            byte high = Byte.parseByte(string.substring(i * 2, i * 2 + 1), 16);
            byte low = Byte.parseByte(string.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte) (high << 4 | low);
        }
        String result = new String(bytes, "gbk");
        return result;
    }

    private void restartApp() {
        Log.i("TAG_Service", "重启app");
       /* Intent activity = new Intent(getApplicationContext(), MainActivity.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activity);*/
        Intent i = getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage(getApplicationContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getApplicationContext().startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
        // android.os.Process.killProcess(Process.myPid());
    }

   /* private void reboot() {
        Log.i("TAG_", "reboot（）");
        new Thread(new Runnable() {
            @Override
            public void run() {
                CardService cardService = null;
                try {
                    cardService = (CardService) new AidlUtils
                            .Builder(getApplicationContext())
                            .setAction("com.xixun.joey.aidlset.SettingsService")
                            .setToPackageName("com.xixun.joey.cardsystem")
                            .setClazz(CardService.class)
                            .build()
                            .getObject();
                    while (cardService == null) {
                        Thread.sleep(1000);
                        Log.i("TAG_", "cardService == null");
                    }
                    try {
                        Log.i("TAG_", "一秒后重启");
                        cardService.reboot(1);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}