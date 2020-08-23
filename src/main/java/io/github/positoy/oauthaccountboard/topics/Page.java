package io.github.positoy.oauthaccountboard.topics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page {
    String label;
    String url;
    boolean disabled;
}
