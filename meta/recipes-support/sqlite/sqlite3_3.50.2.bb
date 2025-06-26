require sqlite3.inc

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://sqlite3.h;endline=11;md5=786d3dc581eff03f4fd9e4a77ed00c66"

SRC_URI = "http://www.sqlite.org/2025/sqlite-autoconf-${SQLITE_PV}.tar.gz"
SRC_URI[sha256sum] = "84a616ffd31738e4590b65babb3a9e1ef9370f3638e36db220ee0e73f8ad2156"

SRC_URI += "file://0001-Add-the-disable-rpath-configure-script-flag-to-addre.patch"

