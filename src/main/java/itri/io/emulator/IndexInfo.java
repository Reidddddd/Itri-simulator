package itri.io.emulator;


public class IndexInfo {
  private final int oprIndex;             private final boolean oprFlag;
  private final int seqNumIndex;          private final boolean seqNumFlag;
  private final int preOpTimeIndex;       private final boolean preOpTimeFlag;
  private final int postOpTimeIndex;      private final boolean postOpTimeFlag;
  private final int processThrdIndex;     private final boolean processThrdFlag;
  private final int majorOpIndex;         private final boolean majorOpFlag;
  private final int irpFlagIndex;         private final boolean irpFlagFlag;
  private final int devObjIndex;          private final boolean devObjFlag;
  private final int fileObjIndex;         private final boolean fileObjFlag;
  private final int statusIndex;          private final boolean statusFlag;
  private final int lengthIndex;          private final boolean lengthFlag;
  private final int offsetIndex;          private final boolean offsetFlag;
  private final int bufferIndex;          private final boolean bufferFlag;
  private final int other1Index;          private final boolean other1Flag;
  private final int other2Index;          private final boolean other2Flag;
  private final int other3Index;          private final boolean other3Flag;
  private final int other4Index;          private final boolean other4Flag;
  private final int preOpSysTimeIndex;    private final boolean preOpSysTimeFlag;
  private final int postOpSysTimeIndex;   private final boolean postOpSysTimeFlag;
  private final int nameIndex;            private final boolean nameFlag;

  public int getOprIndex() {
    if (!oprFlag) throwException("Opr");
    return oprIndex;
  }

  public int getSeqNumIndex() {
    if (!seqNumFlag) throwException("SeqNum");
    return seqNumIndex;
  }

  public int getPreOpTimeIndex() {
    if (!preOpTimeFlag) throwException("PreOpTime");
    return preOpTimeIndex;
  }

  public int getPostOpTimeIndex() {
    if (!postOpTimeFlag) throwException("PostOpTime");
    return postOpTimeIndex;
  }

  public int getProcessThrdIndex() {
    if (!processThrdFlag) throwException("ProcessThrd");
    return processThrdIndex;
  }

  public int getMajorOpIndex() {
    if (!majorOpFlag) throwException("MajorOp");
    return majorOpIndex;
  }

  public int getIrpFlagIndex() {
    if (!irpFlagFlag) throwException("IrpFlag");
    return irpFlagIndex;
  }

  public int getDevObjIndex() {
    if (!devObjFlag) throwException("DevObj");
    return devObjIndex;
  }

  public int getFileObjIndex() {
    if (!fileObjFlag) throwException("FileObj");
    return fileObjIndex;
  }

  public int getStatusIndex() {
    if (!statusFlag) throwException("Status");
    return statusIndex;
  }

  public int getLengthIndex() {
    if (!lengthFlag) throwException("Length");
    return lengthIndex;
  }

  public int getOffsetIndex() {
    if (!offsetFlag) throwException("Offset");
    return offsetIndex;
  }

  public int getBufferIndex() {
    if (!bufferFlag) throwException("Buffer");
    return bufferIndex;
  }

  public int getOther1Index() {
    if (!other1Flag) throwException("Other1");
    return other1Index;
  }

  public int getOther2Index() {
    if (!other2Flag) throwException("Other2");
    return other2Index;
  }

  public int getOther3Index() {
    if (!other3Flag) throwException("Other3");
    return other3Index;
  }

  public int getOther4Index() {
    if (!other4Flag) throwException("Other4");
    return other4Index;
  }

  public int getPreOpSysTimeIndex() {
    if (!preOpSysTimeFlag) throwException("PreOpSysTime");
    return preOpSysTimeIndex;
  }

  public int getPostOpSysTimeIndex() {
    if (!postOpSysTimeFlag) throwException("PostOpSysTime");
    return postOpSysTimeIndex;
  }

  public int getNameIndex() {
    if (!nameFlag) throwException("Name");
    return nameIndex;
  }
  
  private static void throwException(String msg) {
    throw new UnsupportedOperationException(msg + " is not set. So it is not supported.");
  }

  private IndexInfo(Builder builder) {
    this.oprIndex = builder.oprIndex;                     this.oprFlag = builder.oprFlag;
    this.seqNumIndex = builder.seqNumIndex;               this.seqNumFlag = builder.seqNumFlag;
    this.preOpTimeIndex = builder.preOpTimeIndex;         this.preOpTimeFlag = builder.preOpTimeFlag;
    this.postOpTimeIndex = builder.postOpTimeIndex;       this.postOpTimeFlag = builder.postOpTimeFlag;
    this.processThrdIndex = builder.processThrdIndex;     this.processThrdFlag = builder.processThrdFlag;
    this.majorOpIndex = builder.majorOpIndex;             this.majorOpFlag = builder.majorOpFlag;
    this.irpFlagIndex = builder.irpFlagIndex;             this.irpFlagFlag = builder.irpFlagFlag;
    this.devObjIndex = builder.devObjIndex;               this.devObjFlag = builder.devObjFlag;
    this.fileObjIndex = builder.fileObjIndex;             this.fileObjFlag = builder.fileObjFlag;
    this.statusIndex = builder.statusIndex;               this.statusFlag = builder.statusFlag;
    this.lengthIndex = builder.lengthIndex;               this.lengthFlag = builder.lengthFlag;
    this.offsetIndex = builder.offsetIndex;               this.offsetFlag = builder.offsetFlag;
    this.bufferIndex = builder.bufferIndex;               this.bufferFlag = builder.bufferFlag;
    this.other1Index = builder.other1Index;               this.other1Flag = builder.other1Flag;
    this.other2Index = builder.other2Index;               this.other2Flag = builder.other2Flag;
    this.other3Index = builder.other3Index;               this.other3Flag = builder.other3Flag;
    this.other4Index = builder.other4Index;               this.other4Flag = builder.other4Flag;
    this.preOpSysTimeIndex = builder.preOpSysTimeIndex;   this.preOpSysTimeFlag = builder.preOpSysTimeFlag;
    this.postOpSysTimeIndex = builder.postOpSysTimeIndex; this.postOpSysTimeFlag = builder.postOpSysTimeFlag;
    this.nameIndex = builder.nameIndex;                   this.nameFlag = builder.nameFlag;
  } 

  public static class Builder {
    private final ColumnName name;

    private int oprIndex;             private boolean oprFlag;
    private int seqNumIndex;          private boolean seqNumFlag;
    private int preOpTimeIndex;       private boolean preOpTimeFlag;
    private int postOpTimeIndex;      private boolean postOpTimeFlag;
    private int processThrdIndex;     private boolean processThrdFlag;
    private int majorOpIndex;         private boolean majorOpFlag;
    private int irpFlagIndex;         private boolean irpFlagFlag;
    private int devObjIndex;          private boolean devObjFlag;
    private int fileObjIndex;         private boolean fileObjFlag;
    private int statusIndex;          private boolean statusFlag;
    private int lengthIndex;          private boolean lengthFlag;
    private int offsetIndex;          private boolean offsetFlag;
    private int bufferIndex;          private boolean bufferFlag;
    private int other1Index;          private boolean other1Flag;
    private int other2Index;          private boolean other2Flag;
    private int other3Index;          private boolean other3Flag;
    private int other4Index;          private boolean other4Flag;
    private int preOpSysTimeIndex;    private boolean preOpSysTimeFlag;
    private int postOpSysTimeIndex;   private boolean postOpSysTimeFlag;
    private int nameIndex;            private boolean nameFlag;
    

    public Builder(ColumnName name) {
      this.name = name;
    }

    public Builder setOprIndex() {
      this.oprIndex = name.indexOf(ColumnConstants.OPR);
      oprFlag = true;
      return this;
    }

    public Builder setSeqNumIndex() {
      this.seqNumIndex = name.indexOf(ColumnConstants.SEQ_NUM);
      seqNumFlag = true;
      return this;
    }

    public Builder setPreOpTimeIndex() {
      this.preOpTimeIndex = name.indexOf(ColumnConstants.PRE_OP_TIME);
      preOpTimeFlag = true;
      return this;
    }

    public Builder setPostOpTimeIndex() {
      this.postOpTimeIndex = name.indexOf(ColumnConstants.POST_OP_TIME);
      postOpTimeFlag = true;
      return this;
    }

    public Builder setProcessThrdIndex() {
      this.processThrdIndex = name.indexOf(ColumnConstants.PROCESS_THRD);
      processThrdFlag = true;
      return this;
    }

    public Builder setMajorOpIndex() {
      this.majorOpIndex = name.indexOf(ColumnConstants.MAJOR_OP);
      majorOpFlag = true;
      return this;
    }

    public Builder setIrpFlagIndex() {
      this.irpFlagIndex = name.indexOf(ColumnConstants.IRP_FLAGS);
      irpFlagFlag = true;
      return this;
    }

    public Builder setDevObjIndex() {
      this.devObjIndex = name.indexOf(ColumnConstants.DEV_OBJ);
      devObjFlag = true;
      return this;
    }

    public Builder setFileObjIndex() {
      this.fileObjIndex = name.indexOf(ColumnConstants.FILE_OBJ);
      fileObjFlag = true;
      return this;
    }

    public Builder setStatusIndex() {
      this.statusIndex = name.indexOf(ColumnConstants.STATUS);
      statusFlag = true;
      return this;
    }

    public Builder setLengthIndex() {
      this.lengthIndex = name.indexOf(ColumnConstants.LENGTH);
      lengthFlag = true;
      return this;
    }

    public Builder setOffsetIndex() {
      this.offsetIndex = name.indexOf(ColumnConstants.OFFSET);
      offsetFlag = true;
      return this;
    }

    public Builder setBufferIndex() {
      this.bufferIndex = name.indexOf(ColumnConstants.BUFFER);
      bufferFlag = true;
      return this;
    }

    public Builder setOther1Index() {
      this.other1Index = name.indexOf(ColumnConstants.OTHER1);
      other1Flag = true;
      return this;
    }

    public Builder setOther2Index() {
      this.other2Index = name.indexOf(ColumnConstants.OTHER2);
      other2Flag = true;
      return this;
    }

    public Builder setOther3Index() {
      this.other3Index = name.indexOf(ColumnConstants.OTHER3);
      other3Flag = true;
      return this;
    }

    public Builder setOther4Index() {
      this.other4Index = name.indexOf(ColumnConstants.OTHER4);
      other4Flag = true;
      return this;
    }

    public Builder setPreOpSysTimeIndex() {
      this.preOpSysTimeIndex = name.indexOf(ColumnConstants.PRE_OP_SYSTIME);
      preOpSysTimeFlag = true;
      return this;
    }

    public Builder setPostOpSysTimeIndex() {
      this.postOpSysTimeIndex = name.indexOf(ColumnConstants.POST_OP_SYSTIME);
      postOpSysTimeFlag = true;
      return this;
    }

    public Builder setNameIndex() {
      this.nameIndex = name.indexOf(ColumnConstants.NAME);
      nameFlag = true;
      return this;
    }

    public IndexInfo build() {
      return new IndexInfo(this);
    }
  }
}
