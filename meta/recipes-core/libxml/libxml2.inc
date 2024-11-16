SUMMARY = "XML C Parser Library and Toolkit"
DESCRIPTION = "The XML Parser Library allows for manipulation of XML files.  Libxml2 exports Push and Pull type parser interfaces for both XML and HTML.  It can do DTD validation at parse time, on a parsed document instance or with an arbitrary DTD.  Libxml2 includes complete XPath, XPointer and Xinclude implementations.  It also has a SAX like interface, which is designed to be compatible with Expat."
HOMEPAGE = "https://gitlab.gnome.org/GNOME/libxml2"
BUGTRACKER = "http://bugzilla.gnome.org/buglist.cgi?product=libxml2"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://Copyright;md5=f437ed9058e8e5135e47c01e973376ba \
                    file://dict.c;beginline=6;endline=15;md5=2b4b7b827d2d8b080372433c4c9c85b6 \
                    file://list.c;beginline=4;endline=13;md5=b9c25b021ccaf287e50060602d20f3a7 \
"

GNOMEBN = "libxml2"
inherit gnomebase

SRC_URI += "file://run-ptest \
           file://install-tests.patch \
           "

SRC_URI[archive.sha256sum] = "65d042e1c8010243e617efb02afda20b85c2160acdbfbcb5b26b80cec6515650"

BBCLASSEXTEND = "native nativesdk"