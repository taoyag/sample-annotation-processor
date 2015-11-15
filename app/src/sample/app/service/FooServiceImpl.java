package sample.app.service;

import sample.annotation.MyService;

@MyService
public class FooServiceImpl implements FooService {

    @Override
    public String getName() {
        return "foo";
    }
}
