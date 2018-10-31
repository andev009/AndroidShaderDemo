#ifndef ANDROIDSHADERDEMO_PREVIEW_RENDER_H
#define ANDROIDSHADERDEMO_PREVIEW_RENDER_H

#include "./../libcommon/CommonTools.h"
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include "pic_preview_texture.h"

//Shader.frag文件内容
static const char* PIC_PREVIEW_FRAG_SHADER_2 =
		"varying highp vec2 v_texcoord;\n"
		"uniform sampler2D yuvTexSampler;\n"
		"void main() {\n"
		"  gl_FragColor = texture2D(yuvTexSampler, v_texcoord);\n"
		"}\n";

//Shader.vert文件内容
static const char* PIC_PREVIEW_VERTEX_SHADER_2 =
		"attribute vec4 position;    \n"
		"attribute vec2 texcoord;   \n"
		"varying vec2 v_texcoord;     \n"
		"void main(void)               \n"
		"{                            \n"
		"   gl_Position = position;  \n"
		"   v_texcoord = texcoord;  \n"
		"}                            \n";

class PicPreviewRender {
protected:
	GLint _backingLeft;
	GLint _backingTop;
	GLint _backingWidth;
	GLint _backingHeight;

	GLuint vertShader;
	GLuint fragShader;

	PicPreviewTexture* picPreviewTexture;
	GLint uniformSampler;
	GLuint program;

	int useProgram();
	int initShaders();
	GLuint compileShader(GLenum type, const char *sources);
	bool checkGlError(const char* op);

public:
	PicPreviewRender();
	virtual ~PicPreviewRender();
	virtual bool init(int width, int height, PicPreviewTexture* picPreviewTexture);
	virtual void render();
	virtual void dealloc();
	void resetRenderSize(int left, int top, int width, int height);
};


#endif //ANDROIDSHADERDEMO_PREVIEW_RENDER_H
