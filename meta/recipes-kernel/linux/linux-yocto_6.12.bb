KBRANCH ?= "v6.12/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion.inc
include recipes-kernel/linux/cve-exclusion_6.12.inc

# board specific branches
KBRANCH:qemuarm  ?= "v6.12/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v6.12/standard/base"
KBRANCH:qemumips ?= "v6.12/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v6.12/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v6.12/standard/base"
KBRANCH:qemuriscv32  ?= "v6.12/standard/base"
KBRANCH:qemux86  ?= "v6.12/standard/base"
KBRANCH:qemux86.104 ?= "v6.12/standard/base"
KBRANCH:qemuloongarch64  ?= "v6.12/standard/base"
KBRANCH:qemumips64 ?= "v6.12/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "511b6848cd392ee9bbc841794a5e905f4db31689"
SRCREV_machine:qemuarm64 ?= "5633e9a98a153afa6b2051b116faef6c5e855bd8"
SRCREV_machine:qemuloongarch64 ?= "5633e9a98a153afa6b2051b116faef6c5e855bd8"
SRCREV_machine:qemumips ?= "afbfb2216d4f16a231b834922a937641864c8931"
SRCREV_machine:qemuppc ?= "5633e9a98a153afa6b2051b116faef6c5e855bd8"
SRCREV_machine:qemuriscv64 ?= "5633e9a98a153afa6b2051b116faef6c5e855bd8"
SRCREV_machine:qemuriscv32 ?= "5633e9a98a153afa6b2051b116faef6c5e855bd8"
SRCREV_machine:qemux86 ?= "5633e9a98a153afa6b2051b116faef6c5e855bd8"
SRCREV_machine:qemux86-64 ?= "5633e9a98a153afa6b2051b116faef6c5e855bd8"
SRCREV_machine:qemumips64 ?= "707ad7179119ed07e5911b61b08b7b11d2c9a352"
SRCREV_machine ?= "5633e9a98a153afa6b2051b116faef6c5e855bd8"
SRCREV_meta ?= "f8cd1607519efd135e4037c84b6e42684b12c5a8"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "df64e51d4ab83244b6a4eb11eb41f89403611e24"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.12/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.12;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.12.36"

PV = "${LINUX_VERSION}+git"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "arm/versatile-pb.dtb"

COMPATIBLE_MACHINE = "^(qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32|qemuloongarch64)$"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall = " cfg/virtio.scc features/drm-bochs/drm-bochs.scc cfg/net/mdio.scc"
KERNEL_FEATURES:append:qemux86 = " cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64 = " cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc features/nf_tables/nft_test.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc features/gpio/sim.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("KERNEL_DEBUG", "True", " features/reproducibility/reproducibility.scc features/debug/debug-btf.scc", "", d)}"
# libteam ptests from meta-oe needs it
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/net/team/team.scc", "", d)}"
# openl2tp tests from meta-networking needs it
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " cgl/cfg/net/l2tp.scc", "", d)}"
KERNEL_FEATURES:append:powerpc = " arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 = " arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le = " arch/powerpc/powerpc-debug.scc"
# Do not add debug info for riscv32, it fails during depmod
# ERROR: modpost: __ex_table+0x17a4 references non-executable section '.debug_loclists'
# Check again during next major version upgrade
KERNEL_FEATURES:remove:riscv32 = "features/debug/debug-kernel.scc"
INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"
