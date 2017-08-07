-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Anamakine: localhost
-- Üretim Zamanı: 12 May 2017, 10:30:47
-- Sunucu sürümü: 5.7.18-0ubuntu0.16.04.1
-- PHP Sürümü: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `cevapp`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `hastaneler`
--

CREATE TABLE `hastaneler` (
  `hastane_id` int(11) NOT NULL,
  `hastane_adi` varchar(100) CHARACTER SET utf8 COLLATE utf8_turkish_ci NOT NULL,
  `hastane_aciklama` varchar(500) CHARACTER SET utf8 COLLATE utf8_turkish_ci NOT NULL,
  `hastane_lat` varchar(20) NOT NULL,
  `hastane_long` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Tablo döküm verisi `hastaneler`
--

INSERT INTO `hastaneler` (`hastane_id`, `hastane_adi`, `hastane_aciklama`, `hastane_lat`, `hastane_long`) VALUES
(1, 'Hattat Hastanesi', '5 dk yapılır teslim edilir', '41.086096', '29.017919'),
(2, 'Acıbadem Etiler Tıp Merkezi', '5 dk da yapılamz', '41.076446', '29.021487'),
(3, 'Özel Neolife Tıp Merkezi', 'ez oni böcektir ye oni çiçektir', '41.070379', '29.019920'),
(4, 'International Etiler Tıp Merkezi', 'aaaaa', '41.079019', '29.030847'),
(5, 'Etiler Sistem Ağız ve Diş Sağlığı Polikliniği', 'ccac', '41.079081', '29.025853'),
(6, 'Boylam Psikiyatri Hastanesi', 'boylam', '41.077155', '29.017883'),
(7, 'Özel Levent Hastanesi', 'Özel Levent Hastanesi', '41.090151', '29.005044'),
(8, 'SOM Tıp Merkezi', 'SOM Tıp Merkezi', '41.087462', '29.018637'),
(9, 'Gültepe Avicenna Hastanesi', 'Gültepe Avicenna Hastanesi', '41.078729', '28.998648'),
(10, 'Estetistanbul Tıp Merkezi', 'Estetistanbul Tıp Merkezi', '41.083915', '29.013950'),
(11, 'Onep Estetik Plastik Cerrahi Kliniği', 'Onep Estetik Plastik Cerrahi Kliniği', '41.083088', '29.013340'),
(13, 'FİRAT HASTANESİ', 'HARİKA HASTANE', '41.047672271728516', '29.07597541809082');

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `hastaneler`
--
ALTER TABLE `hastaneler`
  ADD PRIMARY KEY (`hastane_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `hastaneler`
--
ALTER TABLE `hastaneler`
  MODIFY `hastane_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
