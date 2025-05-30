SUMMARY = "Library for obtaining the call-chain of a program"
DESCRIPTION = "a portable and efficient C programming interface (API) to determine the call-chain of a program"
HOMEPAGE = "http://www.nongnu.org/libunwind"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d80c8ed4062b8339b715f90fa68cc9f"
DEPENDS:append:libc-musl = " libucontext"

SRC_URI = "https://github.com/libunwind/libunwind/releases/download/v${PV}/${BP}.tar.gz \
           file://mips-byte-order.patch \
           file://0001-tests-Garm64-test-sve-signal-check-that-SVE-is-prese.patch \
           file://0002-coredump-use-glibc-or-musl-register-names-as-appropr.patch \
           file://0003-Fixed-miscompilation-of-unw_getcontext-on-ARM.patch \
           file://0004-Rework-inline-aarch64-as-for-setcontext.patch \
           file://0005-Handle-musl-on-PPC32.patch \
           "

SRC_URI[sha256sum] = "ddf0e32dd5fafe5283198d37e4bf9decf7ba1770b6e7e006c33e6df79e6a6157"

inherit autotools multilib_header

COMPATIBLE_HOST:riscv32 = "null"

PACKAGECONFIG ??= ""
PACKAGECONFIG[lzma] = "--enable-minidebuginfo,--disable-minidebuginfo,xz"
PACKAGECONFIG[zlib] = "--enable-zlibdebuginfo,--disable-zlibdebuginfo,zlib"
PACKAGECONFIG[latexdocs] = "--enable-documentation, --disable-documentation, latex2man-native"

EXTRA_OECONF = "--enable-static"

# http://errors.yoctoproject.org/Errors/Details/20487/
ARM_INSTRUCTION_SET:armv4 = "arm"
ARM_INSTRUCTION_SET:armv5 = "arm"

LDFLAGS += "-Wl,-z,relro,-z,now"
LDFLAGS:append:powerpc:libc-musl = " -latomic"

SECURITY_LDFLAGS:append:libc-musl = " -lssp_nonshared"
CACHED_CONFIGUREVARS:append:libc-musl = " LDFLAGS='${LDFLAGS} -lucontext'"

do_install:append () {
	oe_multilib_header libunwind.h
}

BBCLASSEXTEND = "native"

# libunwind-1.8.1/src/elfxx.c:205:44: error: passing argument 3 of '_Uppc32_get_func_addr' from incompatible pointer type [-Wincompatible-pointer-types]
# libunwind-1.8.1/src/elfxx.c:279:52: error: passing argument 3 of '_Uppc32_get_func_addr' from incompatible pointer type [-Wincompatible-pointer-types]
# and others
CFLAGS:append:powerpc:libc-musl = " -Wno-error=incompatible-pointer-types"
