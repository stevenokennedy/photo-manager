package photoman.service.nef.lookup;

public class MakerLookup extends Lookup 
{
	public static final String IFD_MAKER_NOTE_VERSION = "MakerNoteVersion";
	public static final String IFD_ISO = "ISO";
	public static final String IFD_COLOR_MODE = "ColorMode";
	public static final String IFD_QUALITY = "Quality";
	public static final String IFD_WHITE_BALANCE = "WhiteBalance";
	public static final String IFD_SHARPNESS = "Sharpness";
	public static final String IFD_FOCUS_MODE = "FocusMode";
	public static final String IFD_FLASH_SETTING = "FlashSetting";
	public static final String IFD_FLASH_TYPE = "FlashType";
	public static final String IFD_WHITE_BALANCE_FINE_TUNE = "WhiteBalanceFineTune";
	public static final String IFD_WB_RB_LEVELS = "WB_RBLevels";
	public static final String IFD_PROGRAM_SHIFT = "ProgramShift";
	public static final String IFD_EXPOSURE_DIFFERENCE = "ExposureDifference";
	public static final String IFD_ISO_SELECTION = "ISOSelection";
	public static final String IFD_DATA_DUMP = "DataDump";
	public static final String IFD_PREVIEW_IFD = "PreviewIFD";
	public static final String IFD_FLASH_EXPOSURE_COMP = "FlashExposureComp";
	public static final String IFD_ISO_SETTING = "ISOSetting";
	public static final String IFD_COLOR_BALANCE_ANRW_DATA = "ColorBalanceANRWData";
	public static final String IFD_IMAGE_BOUNDARY = "ImageBoundary";
	public static final String IFD_EXTERNAL_FLASH_EXPOSURE_COMP = "ExternalFlashExposureComp";
	public static final String IFD_FLASH_EXPOSURE_BRACKET_VALUE = "FlashExposureBracketValue";
	public static final String IFD_EXPOSURE_BRACKET_VALUE = "ExposureBracketValue";
	public static final String IFD_IMAGE_PROCESSING = "ImageProcessing";
	public static final String IFD_CROP_HI_SPEED = "CropHiSpeed";
	public static final String IFD_EXPOSURE_TUNING = "ExposureTuning";
	public static final String IFD_SERIAL_NUMBER = "SerialNumber";
	public static final String IFD_COLOR_SPACE = "ColorSpace";
	public static final String IFD_VR_INFO = "VRInfo";
	public static final String IFD_IMAGE_AUTHENTICATION = "ImageAuthentication";
	public static final String IFD_FACE_DETECT = "FaceDetect";
	public static final String IFD_ACTIVED_LIGHTING = "ActiveD-Lighting";
	public static final String IFD_PICTURE_CONTROL_DATA = "PictureControlData";
	public static final String IFD_WORLD_TIME = "WorldTime";
	public static final String IFD_ISO_INFO = "ISOInfo";
	public static final String IFD_VIGNETTE_CONTROL = "VignetteControl";
	public static final String IFD_DISTORT_INFO = "DistortInfo";
	public static final String IFD_UNKNOWN_INFO = "UnknownInfo";
	public static final String IFD_UNKNOWN_INFO2 = "UnknownInfo2";
	public static final String IFD_HDR_INFO = "HDRInfo";
	public static final String IFD_LOCATION_INFO = "LocationInfo";
	public static final String IFD_BLACK_LEVEL = "BlackLevel";
	public static final String IFD_IMAGE_ADJUSTMENT = "ImageAdjustment";
	public static final String IFD_TONE_COMP = "ToneComp";
	public static final String IFD_AUXILIARY_LENS = "AuxiliaryLens";
	public static final String IFD_LENS_TYPE = "LensType";
	public static final String IFD_LENS = "Lens";
	public static final String IFD_MANUAL_FOCUS_DISTANCE = "ManualFocusDistance";
	public static final String IFD_DIGITAL_ZOOM = "DigitalZoom";
	public static final String IFD_FLASH_MODE = "FlashMode";
	public static final String IFD_AF_INFO = "AFInfo";
	public static final String IFD_SHOOTING_MODE = "ShootingMode";
	public static final String IFD_LENS_F_STOPS = "LensFStops";
	public static final String IFD_CONTRAST_CURVE = "ContrastCurve";
	public static final String IFD_COLOR_HUE = "ColorHue";
	public static final String IFD_SCENE_MODE = "SceneMode";
	public static final String IFD_LIGHT_SOURCE = "LightSource";
	public static final String IFD_SHOT_INFO = "ShotInfo";
	public static final String IFD_HUE_ADJUSTMENT = "HueAdjustment";
	public static final String IFD_N_E_F_COMPRESSION = "NEFCompression";
	public static final String IFD_SATURATION = "Saturation";
	public static final String IFD_NOISE_REDUCTION = "NoiseReduction";
	public static final String IFD_N_E_F_LINEARIZATION_TABLE = "NEFLinearizationTable";
	public static final String IFD_COLOR_BALANCE = "ColorBalance"; 
	public static final String IFD_LENS_DATA = "LensData0100"; 
	public static final String IFD_RAW_IMAGE_CENTER = "RawImageCenter";
	public static final String IFD_SENSOR_PIXEL_SIZE = "SensorPixelSize";
	public static final String IFD_SCENE_ASSIST = "SceneAssist";
	public static final String IFD_RETOUCH_HISTORY = "RetouchHistory";
	public static final String IFD_IMAGE_DATA_SIZE = "ImageDataSize";
	public static final String IFD_IMAGE_COUNT = "ImageCount";
	public static final String IFD_DELETED_IMAGE_COUNT = "DeletedImageCount";
	public static final String IFD_SHUTTER_COUNT = "ShutterCount";
	public static final String IFD_FLASH_INFO = "FlashInfo"; 
	public static final String IFD_IMAGE_OPTIMIZATION = "ImageOptimization";
	public static final String IFD_VARI_PROGRAM = "VariProgram";
	public static final String IFD_IMAGE_STABILIZATION = "ImageStabilization";
	public static final String IFD_A_F_RESPONSE = "AFResponse";
	public static final String IFD_MULTI_EXPOSURE = "MultiExposure";
	public static final String IFD_HIGH_I_S_O_NOISE_REDUCTION = "HighISONoiseReduction";
	public static final String IFD_TONING_EFFECT = "ToningEffect";
	public static final String IFD_POWER_UP_TIME = "PowerUpTime";
	public static final String IFD_AF_INFO2 = "AFInfo2";
	public static final String IFD_FILE_INFO = "FileInfo";
	public static final String IFD_AF_TUNE = "AFTune";
	public static final String IFD_RETOUCH_INFO = "RetouchInfo";	
	public static final String IFD_BAROMETER_INFO = "BarometerInfo";
	public static final String IFD_PRINT_IM = "PrintIM";
	public static final String IFD_NIKON_CAPTURE_DATA = "NikonCaptureData";
	public static final String IFD_NIKON_CAPTURE_VERSION = "NikonCaptureVersion";
	public static final String IFD_NIKON_CAPTURE_OFFSETS = "NikonCaptureOffsets";
	public static final String IFD_NIKON_SCAN_IFD = "NikonScanIFD";
	public static final String IFD_NIKON_CAPTURE_EDIT_VERSIONS = "NikonCaptureEditVersions";
	public static final String IFD_NIKON_ICC_PROFILE = "NikonICCProfile";
	public static final String IFD_NIKON_CAPTURE_OUTPUT = "NikonCaptureOutput";
	public static final String IFD_NEF_BIT_DEPTH = "NEFBitDepth";
	
	public MakerLookup()
	{
		ENTRY.put(0x0001, IFD_MAKER_NOTE_VERSION);
		ENTRY.put(0x0002, IFD_ISO);
		ENTRY.put(0x0003, IFD_COLOR_MODE);
		ENTRY.put(0x0004, IFD_QUALITY);
		ENTRY.put(0x0005, IFD_WHITE_BALANCE);
		ENTRY.put(0x0006, IFD_SHARPNESS);
		ENTRY.put(0x0007, IFD_FOCUS_MODE);
		ENTRY.put(0x0008, IFD_FLASH_SETTING);
		ENTRY.put(0x0009, IFD_FLASH_TYPE);
		ENTRY.put(0x000b, IFD_WHITE_BALANCE_FINE_TUNE);
		ENTRY.put(0x000c, IFD_WB_RB_LEVELS);
		ENTRY.put(0x000d, IFD_PROGRAM_SHIFT);
		ENTRY.put(0x000e, IFD_EXPOSURE_DIFFERENCE);
		ENTRY.put(0x000f, IFD_ISO_SELECTION);
		ENTRY.put(0x0010, IFD_DATA_DUMP);
		ENTRY.put(0x0011, IFD_PREVIEW_IFD);
		ENTRY.put(0x0012, IFD_FLASH_EXPOSURE_COMP);
		ENTRY.put(0x0013, IFD_ISO_SETTING);
		ENTRY.put(0x0014, IFD_COLOR_BALANCE_ANRW_DATA);
		ENTRY.put(0x0016, IFD_IMAGE_BOUNDARY);
		ENTRY.put(0x0017, IFD_EXTERNAL_FLASH_EXPOSURE_COMP);
		ENTRY.put(0x0018, IFD_FLASH_EXPOSURE_BRACKET_VALUE);
		ENTRY.put(0x0019, IFD_EXPOSURE_BRACKET_VALUE);
		ENTRY.put(0x001a, IFD_IMAGE_PROCESSING);
		ENTRY.put(0x001b, IFD_CROP_HI_SPEED);
		ENTRY.put(0x001c, IFD_EXPOSURE_TUNING);
		ENTRY.put(0x001d, IFD_SERIAL_NUMBER);
		ENTRY.put(0x001e, IFD_COLOR_SPACE);
		ENTRY.put(0x001f, IFD_VR_INFO);
		ENTRY.put(0x0020, IFD_IMAGE_AUTHENTICATION);
		ENTRY.put(0x0021, IFD_FACE_DETECT);
		ENTRY.put(0x0022, IFD_ACTIVED_LIGHTING);
		ENTRY.put(0x0023, IFD_PICTURE_CONTROL_DATA);
		ENTRY.put(0x0024, IFD_WORLD_TIME);
		ENTRY.put(0x0025, IFD_ISO_INFO);
		ENTRY.put(0x002a, IFD_VIGNETTE_CONTROL);
		ENTRY.put(0x002b, IFD_DISTORT_INFO);
		ENTRY.put(0x002c, IFD_UNKNOWN_INFO);
		ENTRY.put(0x0032, IFD_UNKNOWN_INFO2);
		ENTRY.put(0x0035, IFD_HDR_INFO);
		ENTRY.put(0x0039, IFD_LOCATION_INFO);
		ENTRY.put(0x003d, IFD_BLACK_LEVEL);
		ENTRY.put(0x0080, IFD_IMAGE_ADJUSTMENT);
		ENTRY.put(0x0081, IFD_TONE_COMP);
		ENTRY.put(0x0082, IFD_AUXILIARY_LENS);
		ENTRY.put(0x0083, IFD_LENS_TYPE);
		ENTRY.put(0x0084, IFD_LENS);
		ENTRY.put(0x0085, IFD_MANUAL_FOCUS_DISTANCE);
		ENTRY.put(0x0086, IFD_DIGITAL_ZOOM);
		ENTRY.put(0x0087, IFD_FLASH_MODE);
		ENTRY.put(0x0088, IFD_AF_INFO);
		ENTRY.put(0x0089, IFD_SHOOTING_MODE);
		ENTRY.put(0x008b, IFD_LENS_F_STOPS);
		ENTRY.put(0x008c, IFD_CONTRAST_CURVE);
		ENTRY.put(0x008d, IFD_COLOR_HUE);
		ENTRY.put(0x008f, IFD_SCENE_MODE);
		ENTRY.put(0x0090, IFD_LIGHT_SOURCE);
		ENTRY.put(0x0091, IFD_SHOT_INFO);
		ENTRY.put(0x0092, IFD_HUE_ADJUSTMENT);
		ENTRY.put(0x0093, IFD_N_E_F_COMPRESSION);
		ENTRY.put(0x0094, IFD_SATURATION);
		ENTRY.put(0x0095, IFD_NOISE_REDUCTION);
		ENTRY.put(0x0096, IFD_N_E_F_LINEARIZATION_TABLE);
		ENTRY.put(0x0097, IFD_COLOR_BALANCE);
		ENTRY.put(0x0098, IFD_LENS_DATA);
		ENTRY.put(0x0099, IFD_RAW_IMAGE_CENTER);
		ENTRY.put(0x009a, IFD_SENSOR_PIXEL_SIZE);
		ENTRY.put(0x009c, IFD_SCENE_ASSIST);
		ENTRY.put(0x009e, IFD_RETOUCH_HISTORY);
		ENTRY.put(0x00a0, IFD_SERIAL_NUMBER);
		ENTRY.put(0x00a2, IFD_IMAGE_DATA_SIZE);
		ENTRY.put(0x00a5, IFD_IMAGE_COUNT);
		ENTRY.put(0x00a6, IFD_DELETED_IMAGE_COUNT);
		ENTRY.put(0x00a7, IFD_SHUTTER_COUNT);
		ENTRY.put(0x00a8, IFD_FLASH_INFO);
		ENTRY.put(0x00a9, IFD_IMAGE_OPTIMIZATION);
		ENTRY.put(0x00aa, IFD_SATURATION);
		ENTRY.put(0x00ab, IFD_VARI_PROGRAM);
		ENTRY.put(0x00ac, IFD_IMAGE_STABILIZATION);
		ENTRY.put(0x00ad, IFD_A_F_RESPONSE);
		ENTRY.put(0x00b0, IFD_MULTI_EXPOSURE);
		ENTRY.put(0x00b1, IFD_HIGH_I_S_O_NOISE_REDUCTION);
		ENTRY.put(0x00b3, IFD_TONING_EFFECT);
		ENTRY.put(0x00b6, IFD_POWER_UP_TIME);
		ENTRY.put(0x00b7, IFD_AF_INFO2);
		ENTRY.put(0x00b8, IFD_FILE_INFO);
		ENTRY.put(0x00b9, IFD_AF_TUNE);
		ENTRY.put(0x00bb, IFD_RETOUCH_INFO);
		ENTRY.put(0x00bd, IFD_PICTURE_CONTROL_DATA);
		ENTRY.put(0x00c3, IFD_BAROMETER_INFO);
		ENTRY.put(0x0e00, IFD_PRINT_IM);
		ENTRY.put(0x0e01, IFD_NIKON_CAPTURE_DATA);
		ENTRY.put(0x0e09, IFD_NIKON_CAPTURE_VERSION);
		ENTRY.put(0x0e0e, IFD_NIKON_CAPTURE_OFFSETS);
		ENTRY.put(0x0e10, IFD_NIKON_SCAN_IFD);
		ENTRY.put(0x0e13, IFD_NIKON_CAPTURE_EDIT_VERSIONS);
		ENTRY.put(0x0e1d, IFD_NIKON_ICC_PROFILE);
		ENTRY.put(0x0e1e, IFD_NIKON_CAPTURE_OUTPUT);
		ENTRY.put(0x0e22, IFD_NEF_BIT_DEPTH);
	}
}