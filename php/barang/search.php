<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
$sv = $db::$svr . "/ukm_digital/barang/gambar/";


//cari dengan nama barang
if (isset($_POST['nama']) && !isset($_POST['harga0']) && !isset($_POST['harga1']) && !isset($_POST['jenis_barang'])) {
	# code...
	$nama = $_POST['nama'];
	$sql = "SELECT * from barang where nama_barang like '%$nama%' ORDER BY id_barang DESC";
	$barang_result = mysql_query ($sql) or die(mysql_error());

	if (mysql_num_rows($barang_result) > 0) {
		# code...
		$response["barang"] = array();
		while ($row = mysql_fetch_array($barang_result)) {
			# code...
			$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		$response['message'] = "tidak ada barang";
		echo json_encode($response);
	}
}
// cari dengan harga barang
if (!isset($_POST['nama']) && isset($_POST['harga0']) && isset($_POST['harga1']) && !isset($_POST['jenis_barang'])) {
	# code...
	$harga0 = $_POST['harga0'];
	$harga1 = $_POST['harga1'];
	
	//cari harga di bawah harga0
	if ($harga0 > 0 && $harga1 == 0) {
		# code...
		$sql = "SELECT * from barang where harga < '$harga0' ORDER BY id_barang DESC";
		// echo "di bawah";
	}

	//cari harga di atas harga1
	if ($harga0 == 0 && $harga1 > 0) {
		# code...
		$sql = "SELECT * from barang where harga > '$harga1' ORDER BY id_barang DESC";
		// echo "di atas";
	}

	// cari harga diantara harga0 dan harga1
	if($harga0 > 0 && $harga1 > 0){
		$sql = "SELECT * from barang where harga between '$harga0' and '$harga1' ORDER BY id_barang DESC";
		// echo "di antara";
	}

	$barang_result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($barang_result) > 0) {
		# code...
		$response['barang'] = array();
		while ($row = mysql_fetch_array($barang_result)) {
			# code...
			$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		$response['message'] = "tidak ada barang";
		echo json_encode($response);
	}
}
//cari dengan jenis barang
if (!isset($_POST['nama']) && !isset($_POST['harga0']) && !isset($_POST['harga1']) && isset($_POST['jenis_barang'])) {
	# code...
	$jenis_barang = $_POST['jenis_barang'];
	$query = "SELECT id_jenis_barang from jenis_barang where jenis_barang like '$jenis_barang' ";
	$result = mysql_query($query) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$response['jenis_barang'] = array();
		while ($row = mysql_fetch_array($result)) {
			# code...
			$jenis_barang = array();
			$jenis_barang['id_jenis_barang'] = $row['id_jenis_barang'];
			array_push($response['jenis_barang'], $jenis_barang);
		}
	}

	$id_jenis_barang = $jenis_barang['id_jenis_barang'];
	$sql = "SELECT * from barang where id_jenis_barang = '$id_jenis_barang' ORDER BY id_barang DESC";
	$barang_result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($barang_result) > 0) {
		# code...
		$response['barang'] = array();
		while ($row = mysql_fetch_array($barang_result)) {
			# code...
			$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		$response['message'] = "tidak ada barang";
		echo json_encode($response);
	}
}
//cari dengan nama dan harga barang
if (isset($_POST['nama']) && isset($_POST['harga0']) && isset($_POST['harga1']) && !isset($_POST['jenis_barang'])) {
	# code...
	$nama = $_POST['nama'];
	$harga0 = $_POST['harga0'];
	$harga1 = $_POST['harga1'];
	if ($harga0 > 0 && $harga1 == 0) {
		# code...
		// echo "di bawah";
		$sql = "SELECT * from barang where nama_barang like '%$nama%' && harga < '$harga0' ORDER BY id_barang DESC";
	}
	if ($harga0 == 0 && $harga1 > 0) {
		# code...
		// echo "di atas";
		$sql = "SELECT * from barang where nama_barang like '%$nama%' && harga > '$harga1' ORDER BY id_barang DESC";
	}
	if ($harga0 > 0 && $harga1 > 0) {
		# code...
		// echo "di antara";
		$sql = "SELECT * from barang where nama_barang like '%$nama%' && harga between '$harga0' and '$harga1' ORDER BY id_barang DESC";
	}
	$barang_result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($barang_result) > 0) {
		# code...
		$response['barang'] = array();
		while ($row = mysql_fetch_array($barang_result)) {
			# code...
			$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		$response['message'] = "tidak ada barang";
		echo json_encode($response);
	}
}
//cari dengan nama dan jenis barang
if (isset($_POST['nama']) && !isset($_POST['harga0']) && !isset($_POST['harga1']) && isset($_POST['jenis_barang'])) {
	# code...
	$nama = $_POST['nama'];
	$jenis_barang = $_POST['jenis_barang'];
	$query = "SELECT id_jenis_barang from jenis_barang where jenis_barang like '$jenis_barang' ";
	$result = mysql_query($query) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$response['jenis_barang'] = array();
		while ($row = mysql_fetch_array($result)) {
			# code...
			$jenis_barang = array();
			$jenis_barang['id_jenis_barang'] = $row['id_jenis_barang'];
			array_push($response['jenis_barang'], $jenis_barang);
		}
	}
	$id_jenis_barang = $jenis_barang['id_jenis_barang'];
	$sql = "SELECT * from barang where nama_barang like '%$nama%' && id_jenis_barang = '$id_jenis_barang' ORDER BY id_barang DESC";
	$barang_result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($barang_result) > 0) {
		# code...
		$response['barang'] = array();
		while ($row = mysql_fetch_array($barang_result)) {
			# code...
			$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		$response['message'] = "tidak ada barang";
		echo json_encode($response);
	}
}
//cari dengan harga dan jenis barang
if (!isset($_POST['nama']) && isset($_POST['harga0']) && isset($_POST['harga1']) && isset($_POST['jenis_barang'])) {
	# code...
	$harga0 = $_POST['harga0'];
	$harga1 = $_POST['harga1'];
	$jenis_barang = $_POST['jenis_barang'];

	$query = "SELECT id_jenis_barang from jenis_barang where jenis_barang like '$jenis_barang' ";
	$result = mysql_query($query) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$response['jenis_barang'] = array();
		while ($row = mysql_fetch_array($result)) {
			# code...
			$jenis_barang = array();
			$jenis_barang['id_jenis_barang'] = $row['id_jenis_barang'];
			array_push($response['jenis_barang'], $jenis_barang);
		}
	}
	$id_jenis_barang = $jenis_barang['id_jenis_barang'];
	if ($harga0 > 0 && $harga1 == 0) {
		# code...
		// echo "di bawah";
		$sql = "SELECT * from barang where harga < '$harga0' && id_jenis_barang = '$id_jenis_barang' ORDER BY id_barang DESC";
	}
	if ($harga0 == 0 && $harga1 > 0) {
		# code...
		$sql = "SELECT * from barang where harga > '$harga1' && id_jenis_barang = '$id_jenis_barang' ORDER BY id_barang DESC";
		// echo "di atas";
	}
	if ($harga0 > 0 && $harga1 > 0) {
		# code...
		$sql = "SELECT * from barang where harga between '$harga0' and '$harga1' && id_jenis_barang = '$id_jenis_barang' ORDER BY id_barang DESC";
		// echo "di antara";
	}
	$barang_result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($barang_result) > 0) {
		# code...
		$response['barang'] = array();
		while ($row = mysql_fetch_array($barang_result)) {
			# code...
			$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		$response['message'] = "tidak ada barang";
		echo json_encode($response);
	}
}
//cari dengan nama, harga dan jenis barang
if (isset($_POST['nama']) && isset($_POST['harga0']) && isset($_POST['harga1']) && isset($_POST['jenis_barang'])) {
	# code...
	$nama = $_POST['nama'];
	$harga0 = $_POST['harga0'];
	$harga1 = $_POST['harga1'];
	$jenis_barang = $_POST['jenis_barang'];
	$query = "SELECT id_jenis_barang from jenis_barang where jenis_barang like '$jenis_barang' ";
	$result = mysql_query($query) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$response['jenis_barang'] = array();
		while ($row = mysql_fetch_array($result)) {
			# code...
			$jenis_barang = array();
			$jenis_barang['id_jenis_barang'] = $row['id_jenis_barang'];
			array_push($response['jenis_barang'], $jenis_barang);
		}
	}

	$id_jenis_barang = $jenis_barang['id_jenis_barang'];

	if ($harga0 > 0 && $harga1 == 0) {
		# code...
		// echo "di bawah";
		$sql = "SELECT * from barang where nama_barang like '%$nama%' && harga < '$harga0' && id_jenis_barang = '$id_jenis_barang' ORDER BY id_barang DESC";
	}
	if ($harga0 == 0 && $harga1 > 0) {
		# code...
		// echo "di atas";
		$sql = "SELECT * from barang where nama_barang like '%$nama%' && harga > '$harga1' && id_jenis_barang = '$id_jenis_barang' ORDER BY id_barang DESC";
	}
	if ($harga0 > 0 && $harga1 > 0) {
		# code...
		// echo "di antara";
		$sql = "SELECT * from barang where nama_barang like '%$nama%' && harga between '$harga0' and '$harga1' && id_jenis_barang = '$id_jenis_barang' ORDER BY id_barang DESC";
	}
	$barang_result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($barang_result) > 0) {
		# code...
		$response['barang'] = array();
		while ($row = mysql_fetch_array($barang_result)) {
			# code...
			$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		$response['message'] = "tidak ada barang";
		echo json_encode($response);
	}
}
 ?>