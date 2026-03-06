module.exports = {
  dependency: {
    platforms: {
      android: {
        sourceDir: './android',
        packageImportPath: 'import com.madhur.phonenumberdetector.PhoneNumberHintPackage;',
        packageInstance: 'new PhoneNumberHintPackage()',
      },
      ios: null,
    },
  },
};