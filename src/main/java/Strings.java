/**
 * Nothing more than a storage class for string fields.
 */
public class Strings {
    public static final String main_window_headline = "Welcome to the Paradox Interactive UTF-8 BOM converter tool!";
    public static final String main_window_tool_description = "<html> <p align=\"center\"> This tool was written to check your project for any UTF-8 files that are not yet converted to UTF-8 BOM, and converting them to that encoding if needed. Simply pick your project directory and click go! <br><br>Note: Make a backup, we do not give any guarantee that this tool will not mess up your files!</p>" +
            "<br><br> To start, simply choose the path of your project and click \"Start\". This tool will then check your .txt and .yml files on their encoding, and change them, if necessary. This may take some time.</html>";

    public static final String main_window_path_desc = "Path: ";
    public static final String main_window_path_hint = "Please click here to set your project path";
    public static final String main_window_button_start = "Start";

    public static final String note_success = "Your project was successfully BOMified!";
    public static final String note_empty_path = "The path you provided is empty, please select the correct directory path.";
    public static final String note_not_a_mod = "The path you provided does not seem to contain a Stellaris mod. Please check your directory.";
    public static final String note_not_a_directory = "If you see this message, you *somehow* managed to select a file and not a directory. Kudos to you for breaking this tool.";
    public static final String note_unknown_error = "Something unforseen has happened. Please check your directory for any file changes, or revert to a previous git commit, or go get that backup you made.";
}
