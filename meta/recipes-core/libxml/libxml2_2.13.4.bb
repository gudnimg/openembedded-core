require libxml2.inc

SRC_URI += "http://www.w3.org/XML/Test/xmlts20130923.tar;subdir=${BP};name=testtar"
SRC_URI[testtar.sha256sum] = "c6b2d42ee50b8b236e711a97d68e6c4b5c8d83e69a2be4722379f08702ea7273"

DEPENDS = "zlib virtual/libiconv"

# Disputed as a security issue, but fixed in d39f780
CVE_STATUS[CVE-2023-45322] = "disputed: issue requires memory allocation to fail"

BINCONFIG = "${bindir}/xml2-config"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'ipv6', d)}"
PACKAGECONFIG[ipv6] = "--enable-ipv6,--disable-ipv6,"

inherit autotools pkgconfig binconfig-disabled ptest

LDFLAGS:append:riscv64 = "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-lld ptest', ' -fuse-ld=bfd', '', d)}"

RDEPENDS:${PN}-ptest += "bash make locale-base-en-us"

RDEPENDS:${PN}-ptest:append:libc-musl = " musl-locales"
RDEPENDS:${PN}-ptest:append:libc-glibc = " glibc-gconv-ebcdic-us \
                                           glibc-gconv-ibm1141 \
                                           glibc-gconv-iso8859-5 \
                                           glibc-gconv-euc-jp \
                                         "

# WARNING: zlib is required for RPM use
EXTRA_OECONF = "--without-python --without-debug --without-legacy --with-catalog --with-c14n --without-lzma"
EXTRA_OECONF:class-native = "--without-python --without-legacy --with-c14n --without-lzma --with-zlib"
EXTRA_OECONF:class-nativesdk = "--without-python --without-legacy --with-c14n --without-lzma --with-zlib"
EXTRA_OECONF:linuxstdbase = "--without-python --with-debug --with-legacy --with-c14n --without-lzma --with-zlib"

python populate_packages:prepend () {
    # autonamer would call this libxml2-2, but we don't want that
    if d.getVar('DEBIAN_NAMES'):
        d.setVar('PKG:libxml2', '${MLPREFIX}libxml2')
}

PACKAGE_BEFORE_PN += "${PN}-utils"
FILES:${PN}-utils = "${bindir}/*"

do_configure:prepend () {
	# executables take longer to package: these should not be executable
	find ${S}/xmlconf/ -type f -exec chmod -x {} \+
}

do_install_ptest () {
    oe_runmake DESTDIR=${D} ptestdir=${PTEST_PATH} install-test-data

	cp -r ${S}/xmlconf ${D}${PTEST_PATH}
}

# with musl we need to enable icu support explicitly for these tests
do_install_ptest:append:libc-musl () {
	rm -rf ${D}/${PTEST_PATH}/test/icu_parse_test.xml
}

do_install:append:class-native () {
	# Docs are not needed in the native case
	rm ${D}${datadir}/gtk-doc -rf

	create_wrapper ${D}${bindir}/xmllint 'XML_CATALOG_FILES=${XML_CATALOG_FILES:-${sysconfdir}/xml/catalog}'
}
do_install[vardepsexclude] += "XML_CATALOG_FILES:-${sysconfdir}/xml/catalog"
