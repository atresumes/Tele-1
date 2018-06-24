package com.sinch.relinker.elf;

public interface Elf {

    public abstract class DynamicStructure {
        public static final int DT_NEEDED = 1;
        public static final int DT_NULL = 0;
        public static final int DT_STRTAB = 5;
        public long tag;
        public long val;
    }

    public abstract class Header {
        public static final int ELFCLASS32 = 1;
        public static final int ELFCLASS64 = 2;
        public static final int ELFDATA2MSB = 2;
        public boolean bigEndian;
        public int phentsize;
        public int phnum;
        public long phoff;
        public int shentsize;
        public int shnum;
        public long shoff;
        public int shstrndx;
        public int type;

        public abstract DynamicStructure getDynamicStructure(long j, int i);

        public abstract ProgramHeader getProgramHeader(long j);

        public abstract SectionHeader getSectionHeader(int i);
    }

    public abstract class ProgramHeader {
        public static final int PT_DYNAMIC = 2;
        public static final int PT_LOAD = 1;
        public long memsz;
        public long offset;
        public long type;
        public long vaddr;
    }

    public abstract class SectionHeader {
        public long info;
    }
}
