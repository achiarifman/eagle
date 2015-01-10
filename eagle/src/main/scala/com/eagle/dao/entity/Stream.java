
package com.eagle.dao.entity;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "index",
    "codec_name",
    "codec_long_name",
    "profile",
    "codec_type",
    "codec_time_base",
    "codec_tag_string",
    "codec_tag",
    "width",
    "height",
    "has_b_frames",
    "sample_aspect_ratio",
    "display_aspect_ratio",
    "pix_fmt",
    "level",
    "chroma_location",
    "refs",
    "is_avc",
    "nal_length_size",
    "r_frame_rate",
    "avg_frame_rate",
    "time_base",
    "start_pts",
    "start_time",
    "duration_ts",
    "duration",
    "bit_rate",
    "bits_per_raw_sample",
    "nb_frames",
    "disposition",
    "tags",
    "sample_fmt",
    "sample_rate",
    "channels",
    "channel_layout",
    "bits_per_sample"
})
public class Stream {

    @JsonProperty("index")
    private Integer index;
    @JsonProperty("codec_name")
    private String codecName;
    @JsonProperty("codec_long_name")
    private String codecLongName;
    @JsonProperty("profile")
    private String profile;
    @JsonProperty("codec_type")
    private String codecType;
    @JsonProperty("codec_time_base")
    private String codecTimeBase;
    @JsonProperty("codec_tag_string")
    private String codecTagString;
    @JsonProperty("codec_tag")
    private String codecTag;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("has_b_frames")
    private Integer hasBFrames;
    @JsonProperty("sample_aspect_ratio")
    private String sampleAspectRatio;
    @JsonProperty("display_aspect_ratio")
    private String displayAspectRatio;
    @JsonProperty("pix_fmt")
    private String pixFmt;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("chroma_location")
    private String chromaLocation;
    @JsonProperty("refs")
    private Integer refs;
    @JsonProperty("is_avc")
    private String isAvc;
    @JsonProperty("nal_length_size")
    private String nalLengthSize;
    @JsonProperty("r_frame_rate")
    private String rFrameRate;
    @JsonProperty("avg_frame_rate")
    private String avgFrameRate;
    @JsonProperty("time_base")
    private String timeBase;
    @JsonProperty("start_pts")
    private Integer startPts;
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("duration_ts")
    private Integer durationTs;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("bit_rate")
    private String bitRate;
    @JsonProperty("bits_per_raw_sample")
    private String bitsPerRawSample;
    @JsonProperty("nb_frames")
    private String nbFrames;
    @JsonProperty("disposition")
    private Disposition disposition;
    @JsonProperty("tags")
    private Tags tags;
    @JsonProperty("sample_fmt")
    private String sampleFmt;
    @JsonProperty("sample_rate")
    private String sampleRate;
    @JsonProperty("channels")
    private Integer channels;
    @JsonProperty("channel_layout")
    private String channelLayout;
    @JsonProperty("bits_per_sample")
    private Integer bitsPerSample;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The index
     */
    @JsonProperty("index")
    public Integer getIndex() {
        return index;
    }

    /**
     * 
     * @param index
     *     The index
     */
    @JsonProperty("index")
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * 
     * @return
     *     The codecName
     */
    @JsonProperty("codec_name")
    public String getCodecName() {
        return codecName;
    }

    /**
     * 
     * @param codecName
     *     The codec_name
     */
    @JsonProperty("codec_name")
    public void setCodecName(String codecName) {
        this.codecName = codecName;
    }

    /**
     * 
     * @return
     *     The codecLongName
     */
    @JsonProperty("codec_long_name")
    public String getCodecLongName() {
        return codecLongName;
    }

    /**
     * 
     * @param codecLongName
     *     The codec_long_name
     */
    @JsonProperty("codec_long_name")
    public void setCodecLongName(String codecLongName) {
        this.codecLongName = codecLongName;
    }

    /**
     * 
     * @return
     *     The profile
     */
    @JsonProperty("profile")
    public String getProfile() {
        return profile;
    }

    /**
     * 
     * @param profile
     *     The profile
     */
    @JsonProperty("profile")
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 
     * @return
     *     The codecType
     */
    @JsonProperty("codec_type")
    public String getCodecType() {
        return codecType;
    }

    /**
     * 
     * @param codecType
     *     The codec_type
     */
    @JsonProperty("codec_type")
    public void setCodecType(String codecType) {
        this.codecType = codecType;
    }

    /**
     * 
     * @return
     *     The codecTimeBase
     */
    @JsonProperty("codec_time_base")
    public String getCodecTimeBase() {
        return codecTimeBase;
    }

    /**
     * 
     * @param codecTimeBase
     *     The codec_time_base
     */
    @JsonProperty("codec_time_base")
    public void setCodecTimeBase(String codecTimeBase) {
        this.codecTimeBase = codecTimeBase;
    }

    /**
     * 
     * @return
     *     The codecTagString
     */
    @JsonProperty("codec_tag_string")
    public String getCodecTagString() {
        return codecTagString;
    }

    /**
     * 
     * @param codecTagString
     *     The codec_tag_string
     */
    @JsonProperty("codec_tag_string")
    public void setCodecTagString(String codecTagString) {
        this.codecTagString = codecTagString;
    }

    /**
     * 
     * @return
     *     The codecTag
     */
    @JsonProperty("codec_tag")
    public String getCodecTag() {
        return codecTag;
    }

    /**
     * 
     * @param codecTag
     *     The codec_tag
     */
    @JsonProperty("codec_tag")
    public void setCodecTag(String codecTag) {
        this.codecTag = codecTag;
    }

    /**
     * 
     * @return
     *     The width
     */
    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    @JsonProperty("width")
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 
     * @return
     *     The height
     */
    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The hasBFrames
     */
    @JsonProperty("has_b_frames")
    public Integer getHasBFrames() {
        return hasBFrames;
    }

    /**
     * 
     * @param hasBFrames
     *     The has_b_frames
     */
    @JsonProperty("has_b_frames")
    public void setHasBFrames(Integer hasBFrames) {
        this.hasBFrames = hasBFrames;
    }

    /**
     * 
     * @return
     *     The sampleAspectRatio
     */
    @JsonProperty("sample_aspect_ratio")
    public String getSampleAspectRatio() {
        return sampleAspectRatio;
    }

    /**
     * 
     * @param sampleAspectRatio
     *     The sample_aspect_ratio
     */
    @JsonProperty("sample_aspect_ratio")
    public void setSampleAspectRatio(String sampleAspectRatio) {
        this.sampleAspectRatio = sampleAspectRatio;
    }

    /**
     * 
     * @return
     *     The displayAspectRatio
     */
    @JsonProperty("display_aspect_ratio")
    public String getDisplayAspectRatio() {
        return displayAspectRatio;
    }

    /**
     * 
     * @param displayAspectRatio
     *     The display_aspect_ratio
     */
    @JsonProperty("display_aspect_ratio")
    public void setDisplayAspectRatio(String displayAspectRatio) {
        this.displayAspectRatio = displayAspectRatio;
    }

    /**
     * 
     * @return
     *     The pixFmt
     */
    @JsonProperty("pix_fmt")
    public String getPixFmt() {
        return pixFmt;
    }

    /**
     * 
     * @param pixFmt
     *     The pix_fmt
     */
    @JsonProperty("pix_fmt")
    public void setPixFmt(String pixFmt) {
        this.pixFmt = pixFmt;
    }

    /**
     * 
     * @return
     *     The level
     */
    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    /**
     * 
     * @param level
     *     The level
     */
    @JsonProperty("level")
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 
     * @return
     *     The chromaLocation
     */
    @JsonProperty("chroma_location")
    public String getChromaLocation() {
        return chromaLocation;
    }

    /**
     * 
     * @param chromaLocation
     *     The chroma_location
     */
    @JsonProperty("chroma_location")
    public void setChromaLocation(String chromaLocation) {
        this.chromaLocation = chromaLocation;
    }

    /**
     * 
     * @return
     *     The refs
     */
    @JsonProperty("refs")
    public Integer getRefs() {
        return refs;
    }

    /**
     * 
     * @param refs
     *     The refs
     */
    @JsonProperty("refs")
    public void setRefs(Integer refs) {
        this.refs = refs;
    }

    /**
     * 
     * @return
     *     The isAvc
     */
    @JsonProperty("is_avc")
    public String getIsAvc() {
        return isAvc;
    }

    /**
     * 
     * @param isAvc
     *     The is_avc
     */
    @JsonProperty("is_avc")
    public void setIsAvc(String isAvc) {
        this.isAvc = isAvc;
    }

    /**
     * 
     * @return
     *     The nalLengthSize
     */
    @JsonProperty("nal_length_size")
    public String getNalLengthSize() {
        return nalLengthSize;
    }

    /**
     * 
     * @param nalLengthSize
     *     The nal_length_size
     */
    @JsonProperty("nal_length_size")
    public void setNalLengthSize(String nalLengthSize) {
        this.nalLengthSize = nalLengthSize;
    }

    /**
     * 
     * @return
     *     The rFrameRate
     */
    @JsonProperty("r_frame_rate")
    public String getRFrameRate() {
        return rFrameRate;
    }

    /**
     * 
     * @param rFrameRate
     *     The r_frame_rate
     */
    @JsonProperty("r_frame_rate")
    public void setRFrameRate(String rFrameRate) {
        this.rFrameRate = rFrameRate;
    }

    /**
     * 
     * @return
     *     The avgFrameRate
     */
    @JsonProperty("avg_frame_rate")
    public String getAvgFrameRate() {
        return avgFrameRate;
    }

    /**
     * 
     * @param avgFrameRate
     *     The avg_frame_rate
     */
    @JsonProperty("avg_frame_rate")
    public void setAvgFrameRate(String avgFrameRate) {
        this.avgFrameRate = avgFrameRate;
    }

    /**
     * 
     * @return
     *     The timeBase
     */
    @JsonProperty("time_base")
    public String getTimeBase() {
        return timeBase;
    }

    /**
     * 
     * @param timeBase
     *     The time_base
     */
    @JsonProperty("time_base")
    public void setTimeBase(String timeBase) {
        this.timeBase = timeBase;
    }

    /**
     * 
     * @return
     *     The startPts
     */
    @JsonProperty("start_pts")
    public Integer getStartPts() {
        return startPts;
    }

    /**
     * 
     * @param startPts
     *     The start_pts
     */
    @JsonProperty("start_pts")
    public void setStartPts(Integer startPts) {
        this.startPts = startPts;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    @JsonProperty("start_time")
    public String getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The start_time
     */
    @JsonProperty("start_time")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The durationTs
     */
    @JsonProperty("duration_ts")
    public Integer getDurationTs() {
        return durationTs;
    }

    /**
     * 
     * @param durationTs
     *     The duration_ts
     */
    @JsonProperty("duration_ts")
    public void setDurationTs(Integer durationTs) {
        this.durationTs = durationTs;
    }

    /**
     * 
     * @return
     *     The duration
     */
    @JsonProperty("duration")
    public String getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    @JsonProperty("duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The bitRate
     */
    @JsonProperty("bit_rate")
    public String getBitRate() {
        return bitRate;
    }

    /**
     * 
     * @param bitRate
     *     The bit_rate
     */
    @JsonProperty("bit_rate")
    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    /**
     * 
     * @return
     *     The bitsPerRawSample
     */
    @JsonProperty("bits_per_raw_sample")
    public String getBitsPerRawSample() {
        return bitsPerRawSample;
    }

    /**
     * 
     * @param bitsPerRawSample
     *     The bits_per_raw_sample
     */
    @JsonProperty("bits_per_raw_sample")
    public void setBitsPerRawSample(String bitsPerRawSample) {
        this.bitsPerRawSample = bitsPerRawSample;
    }

    /**
     * 
     * @return
     *     The nbFrames
     */
    @JsonProperty("nb_frames")
    public String getNbFrames() {
        return nbFrames;
    }

    /**
     * 
     * @param nbFrames
     *     The nb_frames
     */
    @JsonProperty("nb_frames")
    public void setNbFrames(String nbFrames) {
        this.nbFrames = nbFrames;
    }

    /**
     * 
     * @return
     *     The disposition
     */
    @JsonProperty("disposition")
    public Disposition getDisposition() {
        return disposition;
    }

    /**
     * 
     * @param disposition
     *     The disposition
     */
    @JsonProperty("disposition")
    public void setDisposition(Disposition disposition) {
        this.disposition = disposition;
    }

    /**
     * 
     * @return
     *     The tags
     */
    @JsonProperty("tags")
    public Tags getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    @JsonProperty("tags")
    public void setTags(Tags tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The sampleFmt
     */
    @JsonProperty("sample_fmt")
    public String getSampleFmt() {
        return sampleFmt;
    }

    /**
     * 
     * @param sampleFmt
     *     The sample_fmt
     */
    @JsonProperty("sample_fmt")
    public void setSampleFmt(String sampleFmt) {
        this.sampleFmt = sampleFmt;
    }

    /**
     * 
     * @return
     *     The sampleRate
     */
    @JsonProperty("sample_rate")
    public String getSampleRate() {
        return sampleRate;
    }

    /**
     * 
     * @param sampleRate
     *     The sample_rate
     */
    @JsonProperty("sample_rate")
    public void setSampleRate(String sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * 
     * @return
     *     The channels
     */
    @JsonProperty("channels")
    public Integer getChannels() {
        return channels;
    }

    /**
     * 
     * @param channels
     *     The channels
     */
    @JsonProperty("channels")
    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    /**
     * 
     * @return
     *     The channelLayout
     */
    @JsonProperty("channel_layout")
    public String getChannelLayout() {
        return channelLayout;
    }

    /**
     * 
     * @param channelLayout
     *     The channel_layout
     */
    @JsonProperty("channel_layout")
    public void setChannelLayout(String channelLayout) {
        this.channelLayout = channelLayout;
    }

    /**
     * 
     * @return
     *     The bitsPerSample
     */
    @JsonProperty("bits_per_sample")
    public Integer getBitsPerSample() {
        return bitsPerSample;
    }

    /**
     * 
     * @param bitsPerSample
     *     The bits_per_sample
     */
    @JsonProperty("bits_per_sample")
    public void setBitsPerSample(Integer bitsPerSample) {
        this.bitsPerSample = bitsPerSample;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
