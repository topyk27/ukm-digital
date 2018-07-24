<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();

if (isset($_POST['id_anggota'])) {
	$id_anggota = $_POST['id_anggota'];

// $sql = "select t.id_transaksi, t.id_barang, t.jumlah_barang, t.total, t.waktu, t.id_anggota, t.status, t.alamat,
// b.nama_barang,
// a.nama
// from transaksi as t, barang as b, anggota as a
// where
// t.id_anggota = a.id_anggota &&
// t.id_barang = b.id_barang &&
// b.id_toko IN(SELECT id_toko FROM toko WHERE id_anggota = '$id_anggota')
// order by status";
	$sql = "select DISTINCT t.id_transaksi, t.waktu, t.status
from
transaksi t,
anggota a,
detail_transaksi dt,
barang b,
toko 
where
t.id_transaksi = dt.id_transaksi &&
b.id_barang = dt.id_barang &&
toko.id_toko = b.id_toko &&
a.id_anggota = toko.id_anggota &&
a.id_anggota = '$id_anggota'
order by t.status, t.waktu DESC ";

$result = mysql_query ($sql) or die(mysql_error()); //run the query

if (mysql_num_rows($result) > 0) {
	$response['penjualan'] = array();
	while ($row = mysql_fetch_array($result)) {
		$pen = array();
		$pen['id_transaksi'] = $row['id_transaksi'];
		// $pen['id_barang'] = $row['id_barang'];
		// $pen['jumlah_barang'] = $row['jumlah_barang'];
		// $pen['total'] = $row['total'];
		// $pen['waktu'] = $row['waktu'];
		// $pen['id_anggota'] = $row['id_anggota'];
		$pen['waktu'] = $row['waktu'];
		$pen['status'] = $row['status'];
		// $pen['alamat'] = $row['alamat'];
		// $pen['nama_barang'] = $row['nama_barang'];
		// $pen['nama'] = $row['nama'];
		array_push($response['penjualan'], $pen);
	}
	$response['success'] = 1;
	echo json_encode($response);
}
else{
	$response["success"] = 0;
	$response["message"] = "tidak ada penjualan";
	echo json_encode($response);
}
}

if (isset($_POST['id_transaksi'])) {
	# code...
	$response['penjualan'] = array();
	$response['detail_transaksi'] = array();
	$id_transaksi = $_POST['id_transaksi'];
	$sql = "select t.id_transaksi, a.id_anggota, a.nama, SUM(dt.jumlah_barang) as jumlah, SUM(dt.total) as total, t.waktu, t.alamat, t.status
from
anggota as a,
transaksi as t,
detail_transaksi as dt
where
t.id_transaksi = '$id_transaksi' &&
a.id_anggota = t.id_anggota &&
t.id_transaksi = dt.id_transaksi";
	
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		
		while ($row = mysql_fetch_array($result)) {
			# code...
			$pen = array();
			$pen['id_transaksi'] = $row['id_transaksi'];
			$pen['id_anggota'] = $row['id_anggota'];
			$pen['nama'] = $row['nama'];
			$pen['jumlah_seluruh'] = $row['jumlah'];
			$pen['total_bayar'] = $row['total'];
			$pen['waktu'] = $row['waktu'];
			$pen['alamat'] = $row['alamat'];
			$pen['status'] = $row['status'];
			array_push($response['penjualan'], $pen);
		}

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
	$response["message"] = "tidak ada penjualan";
	echo json_encode($response);
	}
	}
	else{
		$response["success"] = 0;
	$response["message"] = "tidak ada penjualan";
	echo json_encode($response);
	}
}
 ?>