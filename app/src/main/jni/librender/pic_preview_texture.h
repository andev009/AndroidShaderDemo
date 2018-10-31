#ifndef PIC_PREVIEW_TEXTURE_H
#define PIC_PREVIEW_TEXTURE_H
#include "./../libcommon/CommonTools.h"
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

class PicPreviewTexture {
private:
	GLuint texture;

	int initTexture();

	bool checkGlError(const char* op);
public:
	PicPreviewTexture();
	virtual ~PicPreviewTexture();

	bool createTexture();
	void updateTexImage(byte* pixels, int width, int height);
	bool bindTexture(GLint uniformSampler);
	void dealloc();
};

#endif //PIC_PREVIEW_TEXTURE_H
