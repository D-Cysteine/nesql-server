package com.github.dcysteine.nesql.server.display;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Icon {
    public abstract String getDescription();
    public abstract String getUrl();
    public abstract String getImageFilePath();
    @Nullable public abstract String getTopLeft();
    @Nullable public abstract String getBottomRight();

    public static Builder builder() {
        return new AutoValue_Icon.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setDescription(String description);
        public abstract Builder setUrl(String url);
        public abstract Builder setImageFilePath(String imageFilePath);
        public abstract Builder setTopLeft(@Nullable String topLeft);
        public abstract Builder setBottomRight(@Nullable String bottomRight);

        public abstract Icon build();
    }
}
