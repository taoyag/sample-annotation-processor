package sample.app.service;

import sample.annotation.MyService;

@MyService
public class BarServiceImpl implements BarService {

    @Override
    public String getName() {
        return "bar";
    }
}
