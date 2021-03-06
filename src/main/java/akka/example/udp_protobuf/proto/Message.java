// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Message.proto

package akka.example.udp_protobuf.proto;

/**
 * Protobuf type {@code akka.example.udp_protobuf.proto.Message}
 */
public  final class Message extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:akka.example.udp_protobuf.proto.Message)
    MessageOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Message.newBuilder() to construct.
  private Message(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Message() {
    title_ = "";
    contentSize_ = 0;
    contents_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Message(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            title_ = s;
            break;
          }
          case 16: {

            contentSize_ = input.readInt32();
            break;
          }
          case 26: {
            if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
              contents_ = new java.util.ArrayList<akka.example.udp_protobuf.proto.Content>();
              mutable_bitField0_ |= 0x00000004;
            }
            contents_.add(
                input.readMessage(akka.example.udp_protobuf.proto.Content.parser(), extensionRegistry));
            break;
          }
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
        contents_ = java.util.Collections.unmodifiableList(contents_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return akka.example.udp_protobuf.proto.MessageOuterClass.internal_static_akka_example_udp_protobuf_proto_Message_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return akka.example.udp_protobuf.proto.MessageOuterClass.internal_static_akka_example_udp_protobuf_proto_Message_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            akka.example.udp_protobuf.proto.Message.class, akka.example.udp_protobuf.proto.Message.Builder.class);
  }

  private int bitField0_;
  public static final int TITLE_FIELD_NUMBER = 1;
  private volatile java.lang.Object title_;
  /**
   * <code>string title = 1;</code>
   */
  public java.lang.String getTitle() {
    java.lang.Object ref = title_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      title_ = s;
      return s;
    }
  }
  /**
   * <code>string title = 1;</code>
   */
  public com.google.protobuf.ByteString
      getTitleBytes() {
    java.lang.Object ref = title_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      title_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int CONTENTSIZE_FIELD_NUMBER = 2;
  private int contentSize_;
  /**
   * <code>int32 contentSize = 2;</code>
   */
  public int getContentSize() {
    return contentSize_;
  }

  public static final int CONTENTS_FIELD_NUMBER = 3;
  private java.util.List<akka.example.udp_protobuf.proto.Content> contents_;
  /**
   * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
   */
  public java.util.List<akka.example.udp_protobuf.proto.Content> getContentsList() {
    return contents_;
  }
  /**
   * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
   */
  public java.util.List<? extends akka.example.udp_protobuf.proto.ContentOrBuilder> 
      getContentsOrBuilderList() {
    return contents_;
  }
  /**
   * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
   */
  public int getContentsCount() {
    return contents_.size();
  }
  /**
   * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
   */
  public akka.example.udp_protobuf.proto.Content getContents(int index) {
    return contents_.get(index);
  }
  /**
   * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
   */
  public akka.example.udp_protobuf.proto.ContentOrBuilder getContentsOrBuilder(
      int index) {
    return contents_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getTitleBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, title_);
    }
    if (contentSize_ != 0) {
      output.writeInt32(2, contentSize_);
    }
    for (int i = 0; i < contents_.size(); i++) {
      output.writeMessage(3, contents_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getTitleBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, title_);
    }
    if (contentSize_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, contentSize_);
    }
    for (int i = 0; i < contents_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, contents_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof akka.example.udp_protobuf.proto.Message)) {
      return super.equals(obj);
    }
    akka.example.udp_protobuf.proto.Message other = (akka.example.udp_protobuf.proto.Message) obj;

    boolean result = true;
    result = result && getTitle()
        .equals(other.getTitle());
    result = result && (getContentSize()
        == other.getContentSize());
    result = result && getContentsList()
        .equals(other.getContentsList());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + TITLE_FIELD_NUMBER;
    hash = (53 * hash) + getTitle().hashCode();
    hash = (37 * hash) + CONTENTSIZE_FIELD_NUMBER;
    hash = (53 * hash) + getContentSize();
    if (getContentsCount() > 0) {
      hash = (37 * hash) + CONTENTS_FIELD_NUMBER;
      hash = (53 * hash) + getContentsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static akka.example.udp_protobuf.proto.Message parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static akka.example.udp_protobuf.proto.Message parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static akka.example.udp_protobuf.proto.Message parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static akka.example.udp_protobuf.proto.Message parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(akka.example.udp_protobuf.proto.Message prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code akka.example.udp_protobuf.proto.Message}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:akka.example.udp_protobuf.proto.Message)
      akka.example.udp_protobuf.proto.MessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return akka.example.udp_protobuf.proto.MessageOuterClass.internal_static_akka_example_udp_protobuf_proto_Message_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return akka.example.udp_protobuf.proto.MessageOuterClass.internal_static_akka_example_udp_protobuf_proto_Message_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              akka.example.udp_protobuf.proto.Message.class, akka.example.udp_protobuf.proto.Message.Builder.class);
    }

    // Construct using akka.example.udp_protobuf.proto.Message.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getContentsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      title_ = "";

      contentSize_ = 0;

      if (contentsBuilder_ == null) {
        contents_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
      } else {
        contentsBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return akka.example.udp_protobuf.proto.MessageOuterClass.internal_static_akka_example_udp_protobuf_proto_Message_descriptor;
    }

    @java.lang.Override
    public akka.example.udp_protobuf.proto.Message getDefaultInstanceForType() {
      return akka.example.udp_protobuf.proto.Message.getDefaultInstance();
    }

    @java.lang.Override
    public akka.example.udp_protobuf.proto.Message build() {
      akka.example.udp_protobuf.proto.Message result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public akka.example.udp_protobuf.proto.Message buildPartial() {
      akka.example.udp_protobuf.proto.Message result = new akka.example.udp_protobuf.proto.Message(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.title_ = title_;
      result.contentSize_ = contentSize_;
      if (contentsBuilder_ == null) {
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          contents_ = java.util.Collections.unmodifiableList(contents_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.contents_ = contents_;
      } else {
        result.contents_ = contentsBuilder_.build();
      }
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return (Builder) super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof akka.example.udp_protobuf.proto.Message) {
        return mergeFrom((akka.example.udp_protobuf.proto.Message)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(akka.example.udp_protobuf.proto.Message other) {
      if (other == akka.example.udp_protobuf.proto.Message.getDefaultInstance()) return this;
      if (!other.getTitle().isEmpty()) {
        title_ = other.title_;
        onChanged();
      }
      if (other.getContentSize() != 0) {
        setContentSize(other.getContentSize());
      }
      if (contentsBuilder_ == null) {
        if (!other.contents_.isEmpty()) {
          if (contents_.isEmpty()) {
            contents_ = other.contents_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureContentsIsMutable();
            contents_.addAll(other.contents_);
          }
          onChanged();
        }
      } else {
        if (!other.contents_.isEmpty()) {
          if (contentsBuilder_.isEmpty()) {
            contentsBuilder_.dispose();
            contentsBuilder_ = null;
            contents_ = other.contents_;
            bitField0_ = (bitField0_ & ~0x00000004);
            contentsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getContentsFieldBuilder() : null;
          } else {
            contentsBuilder_.addAllMessages(other.contents_);
          }
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      akka.example.udp_protobuf.proto.Message parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (akka.example.udp_protobuf.proto.Message) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object title_ = "";
    /**
     * <code>string title = 1;</code>
     */
    public java.lang.String getTitle() {
      java.lang.Object ref = title_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        title_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string title = 1;</code>
     */
    public com.google.protobuf.ByteString
        getTitleBytes() {
      java.lang.Object ref = title_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        title_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string title = 1;</code>
     */
    public Builder setTitle(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      title_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string title = 1;</code>
     */
    public Builder clearTitle() {
      
      title_ = getDefaultInstance().getTitle();
      onChanged();
      return this;
    }
    /**
     * <code>string title = 1;</code>
     */
    public Builder setTitleBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      title_ = value;
      onChanged();
      return this;
    }

    private int contentSize_ ;
    /**
     * <code>int32 contentSize = 2;</code>
     */
    public int getContentSize() {
      return contentSize_;
    }
    /**
     * <code>int32 contentSize = 2;</code>
     */
    public Builder setContentSize(int value) {
      
      contentSize_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 contentSize = 2;</code>
     */
    public Builder clearContentSize() {
      
      contentSize_ = 0;
      onChanged();
      return this;
    }

    private java.util.List<akka.example.udp_protobuf.proto.Content> contents_ =
      java.util.Collections.emptyList();
    private void ensureContentsIsMutable() {
      if (!((bitField0_ & 0x00000004) == 0x00000004)) {
        contents_ = new java.util.ArrayList<akka.example.udp_protobuf.proto.Content>(contents_);
        bitField0_ |= 0x00000004;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        akka.example.udp_protobuf.proto.Content, akka.example.udp_protobuf.proto.Content.Builder, akka.example.udp_protobuf.proto.ContentOrBuilder> contentsBuilder_;

    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public java.util.List<akka.example.udp_protobuf.proto.Content> getContentsList() {
      if (contentsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(contents_);
      } else {
        return contentsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public int getContentsCount() {
      if (contentsBuilder_ == null) {
        return contents_.size();
      } else {
        return contentsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public akka.example.udp_protobuf.proto.Content getContents(int index) {
      if (contentsBuilder_ == null) {
        return contents_.get(index);
      } else {
        return contentsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder setContents(
        int index, akka.example.udp_protobuf.proto.Content value) {
      if (contentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureContentsIsMutable();
        contents_.set(index, value);
        onChanged();
      } else {
        contentsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder setContents(
        int index, akka.example.udp_protobuf.proto.Content.Builder builderForValue) {
      if (contentsBuilder_ == null) {
        ensureContentsIsMutable();
        contents_.set(index, builderForValue.build());
        onChanged();
      } else {
        contentsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder addContents(akka.example.udp_protobuf.proto.Content value) {
      if (contentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureContentsIsMutable();
        contents_.add(value);
        onChanged();
      } else {
        contentsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder addContents(
        int index, akka.example.udp_protobuf.proto.Content value) {
      if (contentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureContentsIsMutable();
        contents_.add(index, value);
        onChanged();
      } else {
        contentsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder addContents(
        akka.example.udp_protobuf.proto.Content.Builder builderForValue) {
      if (contentsBuilder_ == null) {
        ensureContentsIsMutable();
        contents_.add(builderForValue.build());
        onChanged();
      } else {
        contentsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder addContents(
        int index, akka.example.udp_protobuf.proto.Content.Builder builderForValue) {
      if (contentsBuilder_ == null) {
        ensureContentsIsMutable();
        contents_.add(index, builderForValue.build());
        onChanged();
      } else {
        contentsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder addAllContents(
        java.lang.Iterable<? extends akka.example.udp_protobuf.proto.Content> values) {
      if (contentsBuilder_ == null) {
        ensureContentsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, contents_);
        onChanged();
      } else {
        contentsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder clearContents() {
      if (contentsBuilder_ == null) {
        contents_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
      } else {
        contentsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public Builder removeContents(int index) {
      if (contentsBuilder_ == null) {
        ensureContentsIsMutable();
        contents_.remove(index);
        onChanged();
      } else {
        contentsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public akka.example.udp_protobuf.proto.Content.Builder getContentsBuilder(
        int index) {
      return getContentsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public akka.example.udp_protobuf.proto.ContentOrBuilder getContentsOrBuilder(
        int index) {
      if (contentsBuilder_ == null) {
        return contents_.get(index);  } else {
        return contentsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public java.util.List<? extends akka.example.udp_protobuf.proto.ContentOrBuilder> 
         getContentsOrBuilderList() {
      if (contentsBuilder_ != null) {
        return contentsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(contents_);
      }
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public akka.example.udp_protobuf.proto.Content.Builder addContentsBuilder() {
      return getContentsFieldBuilder().addBuilder(
          akka.example.udp_protobuf.proto.Content.getDefaultInstance());
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public akka.example.udp_protobuf.proto.Content.Builder addContentsBuilder(
        int index) {
      return getContentsFieldBuilder().addBuilder(
          index, akka.example.udp_protobuf.proto.Content.getDefaultInstance());
    }
    /**
     * <code>repeated .akka.example.udp_protobuf.proto.Content contents = 3;</code>
     */
    public java.util.List<akka.example.udp_protobuf.proto.Content.Builder> 
         getContentsBuilderList() {
      return getContentsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        akka.example.udp_protobuf.proto.Content, akka.example.udp_protobuf.proto.Content.Builder, akka.example.udp_protobuf.proto.ContentOrBuilder> 
        getContentsFieldBuilder() {
      if (contentsBuilder_ == null) {
        contentsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            akka.example.udp_protobuf.proto.Content, akka.example.udp_protobuf.proto.Content.Builder, akka.example.udp_protobuf.proto.ContentOrBuilder>(
                contents_,
                ((bitField0_ & 0x00000004) == 0x00000004),
                getParentForChildren(),
                isClean());
        contents_ = null;
      }
      return contentsBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:akka.example.udp_protobuf.proto.Message)
  }

  // @@protoc_insertion_point(class_scope:akka.example.udp_protobuf.proto.Message)
  private static final akka.example.udp_protobuf.proto.Message DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new akka.example.udp_protobuf.proto.Message();
  }

  public static akka.example.udp_protobuf.proto.Message getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Message>
      PARSER = new com.google.protobuf.AbstractParser<Message>() {
    @java.lang.Override
    public Message parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Message(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Message> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Message> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public akka.example.udp_protobuf.proto.Message getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

