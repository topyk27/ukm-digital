package com.example.topyk.ukmdigital;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topyk.ukmdigital.app.AppController;
import com.example.topyk.ukmdigital.kelas.Kampung;
import com.example.topyk.ukmdigital.menu.anggota;
import com.example.topyk.ukmdigital.menu.dashboard;
import com.example.topyk.ukmdigital.menu.kampung;
import com.example.topyk.ukmdigital.menu.kelola_jenis_barang;
import com.example.topyk.ukmdigital.menu.kelola_kampung;
import com.example.topyk.ukmdigital.menu.login;
import com.example.topyk.ukmdigital.menu.pembelian;
import com.example.topyk.ukmdigital.menu.penjualan;
import com.example.topyk.ukmdigital.menu.toko;
import com.example.topyk.ukmdigital.menu.transaksi;
import com.example.topyk.ukmdigital.menu.profil;
import com.example.topyk.ukmdigital.menu.ukm_digital;
import com.example.topyk.ukmdigital.menu.unmul;
import com.example.topyk.ukmdigital.sub_menu.konfirmasi_beli;
import com.example.topyk.ukmdigital.sub_menu.toko_ku;
import com.example.topyk.ukmdigital.util.JSONParser;
import com.example.topyk.ukmdigital.util.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.topyk.ukmdigital.variabel.url_cek_anggota;
import static com.example.topyk.ukmdigital.variabel.url_read_kampung;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager session;
    int menuygmauditampilkan;
    List<String> listDataHeader;
    String isiKampung;
    HashMap<String, List<String>> listDataChild;
//    ExpandableListView expListView;
//    ExpandListAdapter listAdapter;
    SubMenu subMenu;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    JSONParser jParser = new JSONParser();
    JSONArray daftarKampung = null;
    ArrayList<Kampung> daftar_kampung = new ArrayList<>();
    List<String> id_kampung = new ArrayList<>();
    List<String> nama = new ArrayList<>();
    List<String> alamat = new ArrayList<>();
    List<String> gambar = new ArrayList<>();
    List<LatLng> latlng = new ArrayList<>();
    ArrayList<String> fragaktif1 = new ArrayList<>();
    String[] fragaktif = {"dashboard","toko", "tokoku", "transaksi", "penjualan", "pembelian",
            "anggota", "kampung", "jenis","login","citra","tenun","wadai","ukm","unmul"};
//    String url_read_kampung = "http://192.168.43.192/ukm_digital/kampung/read_kampung.php";

    boolean belanja;
    boolean punyaToko;
    ArrayList<String> id_barangList = new ArrayList<>();
    ArrayList<String> nama_barangList = new ArrayList<>();
    ArrayList<Integer> jumlahBarangList = new ArrayList<>();
    ArrayList<Integer> totalList = new ArrayList<>();
    ArrayList<Integer> stokList = new ArrayList<>();
    ImageView headerUnmul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//coba ambil data dari frag(sebetulnya activity ini direstart)
        Intent i = getIntent();
        if (i.getExtras() != null){
            Bundle e = i.getExtras();
            menuygmauditampilkan = e.getInt("menudarifrag");

        } else {
            menuygmauditampilkan = R.id.dashboard;
        }
        //end

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);
        TextView tvNama = (TextView)v.findViewById(R.id.tvNama);
        TextView tvEmail = (TextView)v.findViewById(R.id.tvEmail);
        NetworkImageView gambarHeader = (NetworkImageView)v.findViewById(R.id.gambarHeader);
        headerUnmul = (ImageView)v.findViewById(R.id.headerUnmul);
//        enableExpandableList();
        final Menu m = navigationView.getMenu();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        subMenu = m.addSubMenu("bla");
//        subMenu = m.addSubMenu(Menu.NONE,R.id.daftarKampung,500,"Daftar Kampung");
        new BacaKampung().execute();
//        Log.d("bacaKampung", bacaKampung());

        subMenu = m.addSubMenu(Menu.NONE,Menu.NONE,500,"Daftar Kampung");

        Log.d("sizeKampung",String.valueOf(id_kampung.size()));

        for (int iter1 = 0; iter1 < fragaktif.length; iter1++){
            fragaktif1.add(fragaktif[iter1]);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                m.findItem(R.id.pembelian).setVisible(false);
                m.findItem(R.id.penjualan).setVisible(false);
                m.findItem(R.id.tokoku).setVisible(false);
                m.findItem(R.id.toko_list).setVisible(false);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        session = new SessionManager(getApplicationContext());
//        Toast.makeText(MainActivity.this,"user : "+ session.getUserDetails().get("id_anggota"), Toast.LENGTH_LONG).show();
        if (this.session.isLoggedIn())
        {
            m.findItem(R.id.login).setVisible(false);
            m.findItem(R.id.logout).setVisible(true);
//            Log.d("namaHeader",session.getUserDetails().get("nama"));
            tvNama.setText(session.getUserDetails().get("nama"));
            tvEmail.setText(session.getUserDetails().get("email"));
            gambarHeader.setImageUrl(session.getUserDetails().get("gambar"),imageLoader);

            Log.d("gambarHeader", session.getUserDetails().get("gambar"));

            m.findItem(R.id.anggota).setVisible(true);
            headerUnmul.setVisibility(View.GONE);



        }
        else
        {
            m.findItem(R.id.login).setVisible(true);
            m.findItem(R.id.logout).setVisible(false);
            m.findItem(R.id.transaksi).setVisible(false);
            gambarHeader.setVisibility(View.GONE);
            tvNama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        }


        if (this.session.isLoggedIn() && !this.session.isAdmin()){
            m.findItem(R.id.anggota).setTitle("Profil");
            m.findItem(R.id.anggota).setIcon(R.drawable.orang);
            new BacaAnggota().execute();
        }

        if (this.session.isAdmin()){
            m.findItem(R.id.kampung).setVisible(true);
            m.findItem(R.id.jenis).setVisible(true);
            m.findItem(R.id.dashboard).setTitle("Kelola Barang");
            m.findItem(R.id.toko).setTitle("Kelola UKM");
            m.findItem(R.id.transaksi).setTitle("Kelola Transaksi");
            m.findItem(R.id.anggota).setIcon(R.drawable.kelola_anggota);

        }

//        tampilmenu(R.id.dashboard);
        tampilmenu(menuygmauditampilkan);



    }

    private String getTag(){ //buat ambil tag biar exit pas di menu

        String tag = null;

        for (int i = 0; i < fragaktif1.size(); i++){
            Fragment fr = getSupportFragmentManager().findFragmentByTag(fragaktif1.get(i));
//            Log.d("aaq",fragaktif[i]);

//            Log.d("qq1", fr.toString());
            if (fr != null && fr.isVisible()){
                tag = fragaktif1.get(i);
                break;
            }
//            Log.d("habis aaq1",tag);
        }

        return tag;
    }

    private String tagFragmentBelanja(){
        String tag = null;
        ArrayList<String> fragbelanja = new ArrayList<>();
        fragbelanja.add("list_barang_toko");
        fragbelanja.add("konfirmasi");
        for (int i = 0; i < fragbelanja.size(); i++){
            Fragment fr = getSupportFragmentManager().findFragmentByTag(fragbelanja.get(i));
            if (fr != null && fr.isVisible()){
                tag = fragbelanja.get(i);
                break;
            }
        }
        return tag;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //kalo mau exit di semua menu utama pake gettag();
//        Fragment f = getSupportFragmentManager().findFragmentByTag("dashboard");

        //ini exit semua
        Fragment f = getSupportFragmentManager().findFragmentByTag(getTag());
        Fragment fb = getSupportFragmentManager().findFragmentByTag(tagFragmentBelanja());

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
//        else {
//            super.onBackPressed();
//        }

        else if(f != null && f.isVisible()){
//            Toast.makeText(this, "boo", Toast.LENGTH_SHORT).show();
//            super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda yakin akan keluar?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if (belanja && fb != null && fb.isVisible()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah anda ingin membatalkan pemesanan?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bersihkan();
                            tampilmenu(R.id.dashboard);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else {
//            getFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack();
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            session.logoutUser();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tampilmenu(int ItemId){
        Fragment f = null;
        String tag = null;


        switch (ItemId){
            case R.id.dashboard:
                f = new dashboard();
                tag = "dashboard";
                break;
            case R.id.toko:
                f = new toko();
                tag = "toko";
                break;
            case R.id.toko_list:
                f = new toko();
                tag = "toko";
                break;

            case R.id.tokoku :
                f = new toko_ku();
                tag = "tokoku";
                break;

            case R.id.transaksi:
                f = new transaksi();
                tag = "transaksi";
                break;
            case R.id.penjualan :
                f = new penjualan();
                tag = "penjualan";
                break;

            case R.id.pembelian :
                f = new pembelian();
                tag = "pembelian";
                break;

            case R.id.anggota :
                if (session.isAdmin()){
                    f = new anggota();
                    tag = "anggota";
                }
                else {
                    f = new profil(); //ntar diganti ke kelola data diri ini cuma tes
                    tag = "anggota";
                }
                break;

            case R.id.kampung :
                f = new kelola_kampung();
                tag = "kampung";
                break;

            case R.id.jenis :
                f = new kelola_jenis_barang();
                tag = "jenis";
                break;

            case R.id.ukm :
                f = new ukm_digital();
                tag = "ukm";
                break;

            case R.id.unmul :
                f = new unmul();
                tag = "unmul";
                break;

            case R.id.login:
                f = new login();
                tag = "login";
                break;
            case R.id.logout:
                session.logoutUser();
                finish();

        }
        if (f != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, f, tag);
            ft.addToBackStack(tag);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        session = new SessionManager(getApplicationContext());
        int id = item.getItemId();

//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);


        if (isiKampung != null && isiKampung.equalsIgnoreCase("ok")){
            for (int i = 0; i < daftarKampung.length(); i++){
                if (id == subMenu.getItem(i).getItemId()){
//                Toast.makeText(MainActivity.this,nama.get(i),Toast.LENGTH_LONG).show();
//                return false;
                    kampung k = new kampung();
                    String tag = nama.get(i);
                    Bundle b = new Bundle();
                    b.putString("id_kampung",id_kampung.get(i));
                    b.putString("nama_kampung", nama.get(i));
                    b.putString("alamat_kampung", alamat.get(i));
                    b.putString("gambar", gambar.get(i));
                    b.putParcelable("latlng", latlng.get(i));
                    k.setArguments(b);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, k, tag);
                    ft.commit();

                }

            }
        }


        if (id == R.id.toko && this.session.isLoggedIn() && !this.session.isAdmin()){
            m.findItem(R.id.tokoku).setVisible(true);
            m.findItem(R.id.toko_list).setVisible(true);
            return false;
        }
        if (id == R.id.transaksi && this.session.isLoggedIn() && !this.session.isAdmin() && !belanja){
            if (punyaToko){
                tampilmenu(R.id.penjualan);
            }
            else {
                tampilmenu(R.id.pembelian);
            }
//            m.findItem(R.id.pembelian).setVisible(true);
//            m.findItem(R.id.penjualan).setVisible(true);
//            return false;
            return true;
        }

//        if (id == subMenu.getItem(0).getItemId()){
//            Toast.makeText(MainActivity.this,"ke1",Toast.LENGTH_LONG).show();
//            return false;
//        }
//        if (id == subMenu.getItem(1).getItemId()){
//            Toast.makeText(MainActivity.this,"ke2",Toast.LENGTH_LONG).show();
//            return false;
//        }


        if (belanja){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah anda ingin membatalkan pemesanan?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bersihkan();
                            tampilmenu(item.getItemId());

                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }
        else {
            tampilmenu(item.getItemId());
            return true;
        }
    }



    class BacaKampung extends AsyncTask<String, Void, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText){
//            bacaKampung();
            isiKampung = bacaKampung();
            return isiKampung;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.d("msiawde",result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(MainActivity.this,
                        "Gagal terhubung, mohon periksa jaringan internet anda",Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("kosong")){
                Toast.makeText(MainActivity.this,
                        "Data kosong",Toast.LENGTH_LONG).show();
            }
            else {

                    for (int iter = 0; iter < daftarKampung.length(); iter++){
                        String nama_kampung = nama.get(iter);
                        nama_kampung = kapital(nama_kampung);
//            subMenu.add(0,Menu.FIRST+iter,Menu.FIRST,nama.get(iter)).setIcon(R.drawable.ic_menu_slideshow);
                        subMenu.add(0,Menu.FIRST+iter,Menu.FIRST,nama_kampung).setIcon(R.drawable.kampung);
                        fragaktif1.add(nama.get(iter));
                    }


//            int in = fragaktif.length;
//            fragaktif[in+1] = nama.get(i);
//            Log.d("fragaktifLength",String.valueOf(in));
//            Log.d("isiFragAktif",fragaktif[in+i]);




            }

        }
    }
    public String bacaKampung(){
        Kampung k = new Kampung();
        List<NameValuePair> p = new ArrayList<>();
        try {
            JSONObject json = jParser.makeHttpRequest(url_read_kampung,"POST", p);

            int success = json.getInt("success");
            Log.d("suksesnya", String.valueOf(success));
            if (success == 1){
                daftarKampung = json.getJSONArray("kampung");
                Log.d("kampungLength", String.valueOf(daftarKampung.length()));
                for (int i = 0; i < daftarKampung.length(); i++){
                    JSONObject c = daftarKampung.getJSONObject(i);
//                    k = new Kampung();
//                    k.setId_kampung(c.getString("id_kampung"));
//                    k.setNama_kampung(c.getString("nama_kampung"));
//                    k.setAlamat_kampung(c.getString("alamat_kampung"));
//                    k.setGambar_kampung(c.getString("gambar"));
//                    daftar_kampung.add(k);
                    id_kampung.add(c.getString("id_kampung"));
                    nama.add(c.getString("nama_kampung"));
                    alamat.add(c.getString("alamat_kampung"));
                    gambar.add(c.getString("gambar"));
                    latlng.add(new LatLng(c.getDouble("lat"), c.getDouble("lng")));
                }
                return "ok";
            }
            else {
                return "kosong";
            }
        }
        catch (Exception e){
            return "exception";
        }
    }

    public String kapital(String str){
        String phrase = "";
        boolean capitalize = true;
        for (char c : str.toLowerCase().toCharArray()) {
            if (Character.isLetter(c) && capitalize) {
                phrase += Character.toUpperCase(c);
                capitalize = false;
                continue;
            } else if (c == ' ') {
                capitalize = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public void reset(){
        id_kampung.clear();
        nama.clear();
        alamat.clear();
        gambar.clear();
        latlng.clear();
        subMenu.removeGroup(0);
        new BacaKampung().execute();
    }

    public void belanja(boolean belanja){
        this.belanja = belanja;

    }
    public boolean getBelanja(){
        return belanja;
    }

    public void id_barangList(ArrayList<String> id_barangList){
        this.id_barangList = id_barangList;
    }
    public void nama_barangList(ArrayList<String> nama_barangList){
        this.nama_barangList = nama_barangList;
    }
    public void jumlahBarangList(ArrayList<Integer> jumlahBarangList){
        this.jumlahBarangList = jumlahBarangList;
    }
    public void totalList(ArrayList<Integer> totalList){
        this.totalList = totalList;
    }
    public void stokList(ArrayList<Integer> stokList){
        this.stokList = stokList;
    }

    public void bersihkan(){
        belanja = false;
        id_barangList.clear();
        nama_barangList.clear();
        jumlahBarangList.clear();
        totalList.clear();
        stokList.clear();
    }

    class BacaAnggota extends AsyncTask<String,Void,String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText){
            List<NameValuePair> p = new ArrayList<>();
            p.add(new BasicNameValuePair("id_anggota",session.getUserDetails().get("id_anggota")));
            try {
                JSONObject json = jParser.makeHttpRequest(url_cek_anggota,"POST",p);
                int success = json.getInt("success");
                if (success == 1){
                    return "ok";
                }
                else {
                    return "kosong";
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return "exception";
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("exception")){
                Toast.makeText(MainActivity.this,
                        "Sambungan terputus, mohon periksa jaringan internet anda",Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("kosong")){
                punyaToko = false;
            }
            else {
                punyaToko = true;
            }
        }
    }

}
