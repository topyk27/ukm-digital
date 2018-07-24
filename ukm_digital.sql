-- phpMyAdmin SQL Dump
-- version 4.7.6
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 07, 2017 at 10:35 AM
-- Server version: 10.1.29-MariaDB
-- PHP Version: 7.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id2893492_ukm_digital`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_admin` int(11) NOT NULL,
  `nama` varchar(128) NOT NULL,
  `alamat` varchar(256) NOT NULL,
  `no_hp` varchar(64) NOT NULL,
  `email` varchar(256) NOT NULL,
  `gambar` text NOT NULL,
  `username` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_admin`, `nama`, `alamat`, `no_hp`, `email`, `gambar`, `username`, `password`) VALUES
(1, 'topyk', 'ini alamat admin', 'ini no hp admin', 'aa@mail.com', 'gak ada gambar.jpg', 'admin', 'admin'),
(27, 'adm', 'adm', '123', 'adm@mail.com', 'az.jpg', 'adm', 'adm');

-- --------------------------------------------------------

--
-- Table structure for table `anggota`
--

CREATE TABLE `anggota` (
  `id_anggota` int(11) NOT NULL,
  `nama` varchar(256) NOT NULL,
  `alamat` varchar(256) NOT NULL,
  `no_hp` varchar(32) NOT NULL,
  `email` varchar(256) NOT NULL,
  `gambar` text NOT NULL,
  `username` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `anggota`
--

INSERT INTO `anggota` (`id_anggota`, `nama`, `alamat`, `no_hp`, `email`, `gambar`, `username`, `password`) VALUES
(17, 'Lusiana Sari', 'jalan kahoi samarinda', '085348716068', 'lusianasari@gmail.com', '0544.jpg', 'lusiana', 'lusianasari'),
(18, 'pak sholiq', 'jalan siti aisyah', '08125809221', 'sholiq@gmail.com', '7b14.jpg', 'sholiq', 'sholiq'),
(19, 'yusfa rahmiani', 'perumahan sambutan', '082153438755', 'rahmiyusfa@yahoo.com', '8845.jpg', 'yusfa', 'yusfarahmiani'),
(20, 'ibu asnawati', 'perumahan bengkuring', '081347709639', 'asnawati@gmail.com', '781f.jpg', 'asnawati', 'asnawati'),
(21, 'jery arisandy', 'karangpaci samarinda', '082245940699', 'jeriarisandy77@gmail.com', 'f9b0.jpg', 'jeryarisandy', 'arisandy'),
(27, 'Taufik D.W.P', 'jalan danau aji no 132 tenggarong', '085250565427', 'taufikdwiwahyuputra@gmail.com', 'a31f.jpg', 'taufik', 'taufik'),
(29, 'riyan hidayat', 'Jl. DI Panjaitan Samarinda', '082357389922', 'riyanvalgore@mail.com', '9795.jpg', 'riyanvalgore', '250594'),
(30, 'Reza maulana', 'bengkuring do sink situ', '085340470778', 'rkajsnjs@maka.coma', 'cbb4.jpg', 'rezayusuf', 'samarinda'),
(32, 'andri candra', 'jalan ramania no 27', '085250103505', 'andri.chandra321@gmail.com', '8a30.jpg', 'andricp', 'asdf123123'),
(33, 'syarief aditya', 'jalan m yamin gg pelayaran', '085321449981', 'aroelgagah@gmail.com', '0cae.jpg', 'sarif00', 'samarinda00'),
(34, 'Adi Pratama', 'Jl. AM Sangaji Gg. 7 Rt. 6 No. 17 Kel. Baru Kec. Tenggarong Kukar', '081253144084', 'adiii.pratama22@gmail.com', '9dca.jpg', 'adipra.tama', 'kosongsembilan'),
(35, 'aya', 'jl. perjuangan 7 ', '085752125951', 'yumria64@gmail.com', '4ecb.jpg', 'riyaaya', '123456'),
(36, 'Muhammad Rizky Caesar Baiquni', 'jalan anggrek merpati 4 no.12 samarinda', '085388445976', 'rizcea21@gmail.com', 'e5fa.jpg', 'rizcea21', 'qwerty123'),
(37, 'Winda KPS', 'jalan danau aji tenggarong', '0812461457 00', 'windkps23@gmail.com', 'aaaa.jpg', 'windakps', 'windakps23'),
(38, 'Hafsiyah', 'jalan gunung gandek no 103 tenggarong', '081350040050', 'hafsiyaah30@gmail.com', 'aaaa1.jpg', 'iyahhafsyiah', 'hafsiyah30'),
(39, 'Sherly Rosalina Vernanda', 'jalan mangkuraja no 28 tenggarong', '085247462240', 'sherly_vr24@gmail.com', '1.jpg', 'sherlyrosalina', 'sherly2409'),
(40, 'Lastry Uthy', 'jalan am alimudin no 34 tenggarong', '081250635257', 'lastryuthy@gmail.com', '11.jpg', '_lastry', 'lastry1995'),
(41, 'Aprian Riska', 'jalan mangkuraja 2 no 14 tenggarong', '085247455087', 'aprianiriskaa29@gmail.com', '12.jpg', 'aprianiriskaa', 'apriani2904'),
(42, 'Nisa Nuril Huda', 'jalan mangkurawang gg 6 no 32 tenggarong', '081350363560', 'nisanurilhuda@gmail.com', '13.jpg', 'nisaanuril', 'nisanuril95'),
(43, 'Reza Maulana Yusuf', 'Bengkuring E-174', '085340470778', 'lalumiere71@gmail.com', '976f.jpg', 'lalumiere71', 'samarinda1'),
(44, 'Hillman Yunardhi', 'JL. K.H. Muksin ', '085348101685', 'hillman_yunardhi@yahoo.com', '4085.jpg', 'hillman', '1234567890'),
(45, 'hayatul mufidah', 'jln. kh. dewantara', '085250353717', 'hayatul25@gmail.com', 'ecf7.jpg', 'fidah', '17101994');

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `id_barang` int(11) NOT NULL,
  `nama_barang` varchar(256) NOT NULL,
  `ukuran` varchar(32) NOT NULL,
  `merk` varchar(32) NOT NULL,
  `khusus` varchar(32) NOT NULL,
  `harga` int(11) NOT NULL,
  `stok` int(11) NOT NULL,
  `deskripsi_barang` text NOT NULL,
  `gambar` text,
  `id_jenis_barang` int(11) NOT NULL,
  `id_toko` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`id_barang`, `nama_barang`, `ukuran`, `merk`, `khusus`, `harga`, `stok`, `deskripsi_barang`, `gambar`, `id_jenis_barang`, `id_toko`) VALUES
(18, 'baju batik kaltim', 'L', '-', '-', 50000, 75, 'baju batik khas kaltim', 'aa7c.jpg', 4, 29),
(19, 'dompet manik', '10 cm x 5 cm', '-', '-', 75000, 11, 'dompet terbuat dari manik manik', 'cef7.jpg', 6, 29),
(20, 'kalung manik', '30 cm', '-', '-', 150000, 71, 'kalung dari bahan manik manik', '1d0c.jpg', 5, 29),
(22, 'tas manik manik', '30 cm x 20 cm', '-', 'Perempuan', 150000, 10, 'tas terbuat dari bahan manik manik', '9ff2.jpg', 6, 30),
(23, 'jam kayu kaltim', '60 cm x 90 cm', '-', '-', 275000, 10, 'jam dinding dari bahan kayu', '4edf.jpg', 3, 30),
(24, 'peci motif batik', 'M', '-', 'Laki-Laki', 40000, 10, 'peci bermotif batik', '4b14.jpg', 1, 31),
(25, 'baju adat kaltim', 'L', '-', 'Laki-Laki dan Perempuan', 350000, 10, 'baju adat kaltim\npria 300000\nwanita 350000', '9f66.jpg', 4, 31),
(27, 'baju oleh oleh kaltim', 'L', '-', '-', 25000, 4, 'baju oleh oleh dari kalimantan timur', 'cbf9.jpg', 4, 32),
(28, 'kalung rumbai', '40 cm x 20 cm', '-', '-', 100000, 3, 'kalung rumbai', '984c.jpg', 5, 32),
(29, 'tas dari rotan', '50 cm x 30 cm', '-', '-', 150000, 4, 'tas yang terbuat dari bahan rotan', 'bdd0.jpg', 8, 32);

-- --------------------------------------------------------

--
-- Table structure for table `detail_transaksi`
--

CREATE TABLE `detail_transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `jumlah_barang` int(11) NOT NULL,
  `total` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_transaksi`
--

INSERT INTO `detail_transaksi` (`id_transaksi`, `id_barang`, `jumlah_barang`, `total`) VALUES
(6, 29, 1, 150000),
(6, 28, 2, 200000),
(6, 27, 3, 75000),
(7, 20, 3, 450000),
(7, 19, 1, 75000),
(8, 28, 2, 200000),
(9, 29, 1, 150000);

-- --------------------------------------------------------

--
-- Table structure for table `jenis_barang`
--

CREATE TABLE `jenis_barang` (
  `id_jenis_barang` int(11) NOT NULL,
  `jenis_barang` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `jenis_barang`
--

INSERT INTO `jenis_barang` (`id_jenis_barang`, `jenis_barang`) VALUES
(1, 'kopiah'),
(3, 'jam'),
(4, 'pakaian'),
(5, 'aksesoris'),
(6, 'manik - manik'),
(7, 'makanan'),
(8, 'tas'),
(9, 'minyak urut');

-- --------------------------------------------------------

--
-- Table structure for table `kampung`
--

CREATE TABLE `kampung` (
  `id_kampung` int(11) NOT NULL,
  `nama_kampung` varchar(128) NOT NULL,
  `alamat_kampung` varchar(256) NOT NULL,
  `gambar` text NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kampung`
--

INSERT INTO `kampung` (`id_kampung`, `nama_kampung`, `alamat_kampung`, `gambar`, `lat`, `lng`) VALUES
(2, 'citra niaga', 'Pertokoan Citra Niaga, Jalan Niaga Utara, Samarinda Kota, Pelabuhan, Samarinda Kota, Kota Samarinda, Kalimantan Timur', '4299.jpg', -0.503053, 117.149515),
(4, 'kampung tenun', 'Jalan Hos Cokro Aminoto, Rapak Dalam, Samarinda Seberang, Kota Samarinda, Kalimantan Timur', '904c.jpg', -0.510949, 117.147298),
(6, 'kampung wadai', 'Jalan Biawan, Sidomulyo, Samarinda Ilir, Kota Samarinda, Kalimantan Timur', 'kampungwadai.jpg', -0.496809, 117.160443);

-- --------------------------------------------------------

--
-- Table structure for table `profil`
--

CREATE TABLE `profil` (
  `id` int(11) NOT NULL,
  `nama` varchar(256) NOT NULL,
  `deskripsi` text NOT NULL,
  `gambar` text NOT NULL,
  `link` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `profil`
--

INSERT INTO `profil` (`id`, `nama`, `deskripsi`, `gambar`, `link`) VALUES
(1, 'UKM Digital', 'Kampung UKM Digital adalah Pemanfaatan teknologi Informasi secara komprehensif dan integratif untuk mendukung proses bisnis yang berjalan di Sentra UKM atau UKM yang terpusat di suatu lokasi tertentu dalam rangka mewujudkan Jutaan UKM yang Maju, Mandiri, dan Modern.', '545d.png', 'www.kampungukmdigital.com'),
(2, 'Universitas Mulawarman', 'Saat ini Universitas Mulawarman telah mendapatkan Akreditasi dengan Nilai: 318 dengan Peringkat B(Baik) dari Badan Akreditasi Nasional Perguruan Tinggi (BAN-PT) sesuai dengan Surat Keputusan Nomor:239/SK/BAN-PT/Akred/PT/VII/2014 Tanggal 19 Juli 2014.', '124f.png', 'unmul.ac.id');

-- --------------------------------------------------------

--
-- Table structure for table `toko`
--

CREATE TABLE `toko` (
  `id_toko` int(11) NOT NULL,
  `nama_toko` varchar(128) NOT NULL,
  `alamat_toko` varchar(256) NOT NULL,
  `telp` varchar(14) NOT NULL,
  `deskripsi_toko` text NOT NULL,
  `gambar` text NOT NULL,
  `id_kampung` int(11) NOT NULL,
  `id_anggota` int(11) NOT NULL,
  `lat` double(10,6) NOT NULL,
  `lng` double(10,6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `toko`
--

INSERT INTO `toko` (`id_toko`, `nama_toko`, `alamat_toko`, `telp`, `deskripsi_toko`, `gambar`, `id_kampung`, `id_anggota`, `lat`, `lng`) VALUES
(29, 'Souvenir Maya', 'kompleks citra niaga blok f no 26 samarinda', '082350844273', 'Menjual kaos kaltim,  batik kaltim,  songket kaltim,  sarung samarinda,  mandau,  tameng,  batu permata,  gelang,  kalung batu,  anjat,  ulap doyo,  manik - manik,  ukiran kayu', '850c.jpg', 2, 17, -0.502901, 117.149719),
(30, 'Souvenir Lestari', 'komplek citra niaga blok f', '08125809221', 'menjual kalung manik,  tas,  anjat,  ulap doyo,  sarung samarinda,  batik, mandau,  sumpit', '46eb.jpg', 2, 18, -0.502899, 117.149680),
(31, 'souvenir raja bagus', 'komplek citra niaga blok f', '082152150400', 'menjual barang kerajinan kalung manik,  tas,  anjat,  kipas,  ulap doyo,  sarung samarinda,  batik,  mandau,  sumpit ', '5d51.jpg', 2, 19, -0.502922, 117.149676),
(32, 'souvenir anugerah', 'komplek citra niaga blok f no 10', '081347709639', 'menjual barang barang kerajinan kalung manik,  tas, anjat,  ulap doyo,  sarung samarinda,  batik,  mandau,  sumpit', '2035.jpg', 2, 20, -0.502829, 117.149606);

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `waktu` date NOT NULL,
  `id_anggota` int(11) NOT NULL,
  `status` enum('menunggu','dikirim','batal','diterima') NOT NULL DEFAULT 'menunggu',
  `alamat` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `total`, `waktu`, `id_anggota`, `status`, `alamat`) VALUES
(6, 425000, '2017-10-13', 27, 'diterima', 'm. yamin gg pelayaran'),
(7, 525000, '2017-10-31', 30, 'diterima', 'sfhdjjkhhdkfhzhjxhj'),
(8, 200000, '2017-10-31', 30, 'menunggu', 'jalan bayam no 24 bengkuring'),
(9, 150000, '2017-11-09', 32, 'batal', 'ramania no.27');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`);

--
-- Indexes for table `anggota`
--
ALTER TABLE `anggota`
  ADD PRIMARY KEY (`id_anggota`);

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id_barang`),
  ADD KEY `id_jenis_barang` (`id_jenis_barang`),
  ADD KEY `id_toko` (`id_toko`);

--
-- Indexes for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  ADD KEY `id_transaksi` (`id_transaksi`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indexes for table `jenis_barang`
--
ALTER TABLE `jenis_barang`
  ADD PRIMARY KEY (`id_jenis_barang`);

--
-- Indexes for table `kampung`
--
ALTER TABLE `kampung`
  ADD PRIMARY KEY (`id_kampung`);

--
-- Indexes for table `profil`
--
ALTER TABLE `profil`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `toko`
--
ALTER TABLE `toko`
  ADD PRIMARY KEY (`id_toko`),
  ADD KEY `id_kampung` (`id_kampung`),
  ADD KEY `id_anggota` (`id_anggota`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_anggota` (`id_anggota`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `anggota`
--
ALTER TABLE `anggota`
  MODIFY `id_anggota` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `barang`
--
ALTER TABLE `barang`
  MODIFY `id_barang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `jenis_barang`
--
ALTER TABLE `jenis_barang`
  MODIFY `id_jenis_barang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `kampung`
--
ALTER TABLE `kampung`
  MODIFY `id_kampung` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `profil`
--
ALTER TABLE `profil`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `toko`
--
ALTER TABLE `toko`
  MODIFY `id_toko` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `barang`
--
ALTER TABLE `barang`
  ADD CONSTRAINT `barang_ibfk_1` FOREIGN KEY (`id_jenis_barang`) REFERENCES `jenis_barang` (`id_jenis_barang`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `barang_ibfk_2` FOREIGN KEY (`id_toko`) REFERENCES `toko` (`id_toko`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  ADD CONSTRAINT `detail_transaksi_ibfk_1` FOREIGN KEY (`id_transaksi`) REFERENCES `transaksi` (`id_transaksi`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_transaksi_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `toko`
--
ALTER TABLE `toko`
  ADD CONSTRAINT `toko_ibfk_1` FOREIGN KEY (`id_kampung`) REFERENCES `kampung` (`id_kampung`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `toko_ibfk_2` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
