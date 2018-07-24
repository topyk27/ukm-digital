<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
$sv = "http://192.168.43.192/ukm_digital/transaksi/gambar/";

if (isset($_POST['id_transaksi'])) {
	# code...
	$id_transaksi = $_POST['id_transaksi'];
	$response['pembeli'] = array();
	$response['penjual'] = array();
	$response['detail_transaksi'] = array();

	//read pemilik(penjual)
	$sql = "select t.id_transaksi, a.id_anggota, a.nama as penjual, SUM(dt.jumlah_barang) as jumlah, SUM(dt.total) as total, t.waktu, t.alamat, t.status
from
transaksi t,
anggota a,
detail_transaksi dt,
barang b,
toko
WHERE
t.id_transaksi = dt.id_transaksi &&
b.id_barang = dt.id_barang &&
toko.id_toko = b.id_toko &&
a.id_anggota = toko.id_anggota &&
t.id_transaksi = '$id_transaksi' ";
//nama itu pemilik barang
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		while ($row = mysql_fetch_array($result)) {
			# code...
			//$beli itu penjual
			$beli = array();
			$beli['id_transaksi'] = $row['id_transaksi'];
			$beli['id_anggota'] = $row['id_anggota'];
			$beli['penjual'] = $row['penjual'];
			$beli['jumlah_seluruh'] = $row['jumlah'];
			$beli['total_bayar'] = $row['total'];
			$beli['waktu'] = $row['waktu'];
			$beli['alamat'] = $row['alamat'];
			$beli['status'] = $row['status'];
			array_push($response['penjual'], $beli);
		}
		//read pembeli
		$q = "select t.id_transaksi, a.id_anggota, a.nama as pembeli, SUM(dt.jumlah_barang) as jumlah, SUM(dt.total) as total, t.waktu, t.alamat, t.status
from
anggota as a,
transaksi as t,
detail_transaksi as dt
where
t.id_transaksi = '$id_transaksi' &&
a.id_anggota = t.id_anggota &&
t.id_transaksi = dt.id_transaksi ";
	$h = mysql_query($q) or die(mysql_error());
	if (mysql_num_rows($h) > 0) {
		# code...
		while ($row = mysql_fetch_array($h)) {
			# code...
			$pembeli = array();
			$pembeli['id_anggota'] = $row['id_anggota'];
			$pembeli['pembeli'] = $row['pembeli'];
			array_push($response['pembeli'], $pembeli);

		}
		//read detail transaksi
		$query = "select b.nama_barang, dt.jumlah_barang, dt.total as harga
from
barang as b,
detail_transaksi as dt
where
b.id_barang = dt.id_barang &&
dt.id_transaksi = '$id_transaksi' ";
	$hasil = mysql_query($query) or die(mysql_error());
	if (mysql_num_rows($hasil) > 0) {
		# code...
		while ($row = mysql_fetch_array($hasil)) {
			# code...
			$p = array();
			$p['nama_barang'] = $row['nama_barang'];
			$p['jumlah_barang'] = $row['jumlah_barang'];
			$p['harga'] = $row['harga'];
			array_push($response['detail_transaksi'], $p);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
	
		echo json_encode($response);
	}
	}
	else{
		$response["success"] = 0;
	
		echo json_encode($response);
	}

	}
	else{
		$response["success"] = 0;
	
		echo json_encode($response);
	}
}

if (!isset($_POST['id_transaksi'])) {
	# code...
	$response['transaksi'] = array();
	$sql = "select id_transaksi, waktu, status from transaksi order by status, waktu DESC";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		while ($row = mysql_fetch_array($result)) {
			# code...
			$t = array();
			$t['id_transaksi'] = $row['id_transaksi'];
			$t['waktu'] = $row['waktu'];
			$t['status'] = $row['status'];
			array_push($response['transaksi'], $t);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		echo json_encode($response);
	}
}


 ?>