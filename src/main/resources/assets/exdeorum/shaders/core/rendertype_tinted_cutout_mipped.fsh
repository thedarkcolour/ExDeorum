#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
in float progress;

out vec4 fragColor;

void main() {
    vec4 oldColor = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (oldColor.a < 0.5) {
        discard;
    }
    float avg = oldColor.r * 0.3 + oldColor.g * 0.59 + oldColor.b * 0.11;

    vec4 color;
    if (progress == 1.0f) {
        color = vec4(avg, avg, avg, progress);
    } else {
        color = vec4(
            mix(oldColor.r, avg, progress),
            mix(oldColor.g, avg, progress),
            mix(oldColor.b, avg, progress),
            oldColor.a
        );
    }
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
